package com.sis.api.push.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sis.api.push.base.MulticastPayload;
import com.sis.api.push.base.PayloadBase;
import com.sis.api.push.base.TokenTargetPayload;
import com.sis.api.push.mapper.DeviceMapper;
import com.sis.api.push.model.AppInfoVO;
import com.sis.api.push.model.DeviceVO;

@Service
public class APIService
{
    
    @Autowired
    DeviceMapper mapper;
    
    public List<DeviceVO> selectDevices(String appid)
    {
        return mapper.selectDevices(appid);
    }
    
    public List<DeviceVO> selectDevice(String appid, String mbr_id)
    {
        
        return mapper.selectDevice(appid, mbr_id);
        
    }
    
    public AppInfoVO apiKey(String appid)
    {
        return mapper.selectApiKey(appid);
    }
    
    public int insertDevice(DeviceVO vo)
    {
        return mapper.insertDevice(vo);
    }
    
    public int updateAgree(String appid, String uuid, String rcvyn) throws Exception
    {
        if ((rcvyn.toUpperCase().equals("Y")) || (rcvyn.toUpperCase().equals("N")))
        {
            return mapper.updateAgree(appid, uuid, rcvyn.toUpperCase());
        }
        throw new Exception("Receive YN should be Y or N");
    }
    
    public int updateMember(String appid, String uuid, String mbrid)
    {
        return mapper.updateMember(appid, uuid, mbrid);
    }
    
    public boolean sendMessage(String appid, String uuid, String mbrid, PayloadBase payload, String[] targets)
    {
        
        if (payload instanceof MulticastPayload)
        {
            
            if (targets == null)
                return false;
            
            if (targets.length < 1000)
            {
                
                ((MulticastPayload) payload).setRegistration_ids(Arrays.asList(targets));
                return sendFCMPush(appid, payload);
            }
            else
            {
                String[][] tokens = splitArray(targets, 1000);
                for (int i = 0; i < tokens.length; i++)
                {
                    String[] items = tokens[i];
                    ((MulticastPayload) payload).setRegistration_ids(Arrays.asList(items));
                    sendFCMPush(appid, payload);
                }
                return true;
            }
            
        }
        else if (payload instanceof TokenTargetPayload)
        {
            
            if (mbrid != null)
            {
                List<DeviceVO> items = mapper.selectDevice(appid, mbrid);
                if (items.size() == 0)
                    return false;
                if (items.size() == 1)
                {
                    ((TokenTargetPayload) payload).setTo(items.get(0).getTOKEN());
                }
                else
                {
                    MulticastPayload pload = buildMulticastPayloadFromPayload(payload);
                    pload.setRegistration_ids(extractTokens(items));
                    return sendFCMPush(appid, pload);
                }
            }
            else if (uuid != null)
            {
                List<DeviceVO> items = mapper.selectDeviceByUUID(appid, uuid);
                if (items.size() == 0)
                    return false;
                if (items.size() == 1)
                {
                    ((TokenTargetPayload) payload).setTo(items.get(0).getTOKEN());
                    return sendFCMPush(appid, payload);
                }
                else
                {
                    MulticastPayload pload = buildMulticastPayloadFromPayload(payload);
                    pload.setRegistration_ids(extractTokens(items));
                    return sendFCMPush(appid, pload);
                }
            }
            else
            {
                
                ((TokenTargetPayload) payload).setTo("/topics/all");
                
                return sendFCMPush(appid, payload);
                
            }
        }
        
        return false;
    }
    
    private List<String> extractTokens(List<DeviceVO> items)
    {
        List<String> data = new ArrayList<String>();
        Iterator<DeviceVO> iter = items.iterator();
        while (iter.hasNext())
        {
            DeviceVO item = iter.next();
            data.add(item.getTOKEN());
        }
        return data;
    }
    
    private MulticastPayload buildMulticastPayloadFromPayload(PayloadBase payload)
    {
        MulticastPayload mpayload = new MulticastPayload();
        mpayload.setNotification(payload.getNotification());
        mpayload.setData(payload.getData());
        return mpayload;
    }
    
    public boolean sendFCMPush(String appid, PayloadBase payload)
    {
        
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(payload);
        
        try
        {
            
            AppInfoVO appInfo = mapper.selectApiKey(appid);
            String apiKey = appInfo.getAPIKEY();
            
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + apiKey);
            
            conn.setDoOutput(true);
            
            OutputStream os = conn.getOutputStream();
            
            // 서버에서 날려서 한글 깨지는 사람은 아래처럼  UTF-8로 인코딩해서 날려주자
            os.write(json.getBytes("UTF-8"));
            os.flush();
            os.close();
            
            int responseCode = conn.getResponseCode();
            
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + json);
            System.out.println("Response Code : " + responseCode);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }
            in.close();
            
            System.out.println(response.toString());
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public static String[][] splitArray(String[] arrayToSplit, int chunkSize)
    {
        if (chunkSize <= 0)
        {
            return null;
        }
        int rest = arrayToSplit.length % chunkSize;
        int chunks = arrayToSplit.length / chunkSize + (rest > 0 ? 1 : 0);
        String[][] arrays = new String[chunks][];
        for (int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++)
        {
            arrays[i] = Arrays.copyOfRange(arrayToSplit, i * chunkSize, i * chunkSize + chunkSize);
        }
        if (rest > 0)
        {
            arrays[chunks - 1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * chunkSize, (chunks - 1) * chunkSize + rest);
        }
        return arrays;
    }
    
}
