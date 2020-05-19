package com.sis.api.push.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sis.api.push.base.MulticastPayload;
import com.sis.api.push.base.PayloadBase;
import com.sis.api.push.base.ResponseBase;
import com.sis.api.push.base.TokenTargetPayload;
import com.sis.api.push.model.DeviceVO;
import com.sis.api.push.security.KeyRequired;
import com.sis.api.push.service.APIService;

@Controller
@RequestMapping("/")
public class APIController
{
    
    @Autowired
    APIService mService;
    
    /**
     * 전체 디바이스 목록 추출
     * 
     * @param appid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/devices", method = RequestMethod.POST)
    @KeyRequired
    public ResponseEntity<List<DeviceVO>> listDevices(@RequestHeader(value = "appid") String appid)
    {
        List<DeviceVO> items = mService.selectDevices(appid);
        return ResponseEntity.ok(items);
    }
    
    /**
     * 회원기반 디바이스 항목 추출
     * 
     * @param appid
     * @param mbr_id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/device", method = RequestMethod.POST)
    @KeyRequired
    public ResponseEntity<List<DeviceVO>> selectDevice(@RequestHeader(value = "appid") String appid, @RequestParam(value = "mbr_id") String mbr_id)
    {
        List<DeviceVO> items = mService.selectDevice(appid, mbr_id);
        return ResponseEntity.ok(items);
    }
    
    /**
     * 디바이스 등록
     * 
     * @param appid
     * @param uuid
     * @param token
     * @param rcvyn
     * @param mbrid
     * @param os
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @KeyRequired
    @ResponseBody
    public ResponseBase register(@RequestHeader(value = "appid") String appid, @RequestParam("uuid") String uuid, @RequestParam("token") String token, @RequestParam(value = "rcvyn", required = false) String rcvyn, @RequestParam(value = "mbrid", required = false) String mbrid,
            @RequestParam(value = "os") String os)
    {
        ResponseBase resp = new ResponseBase();
        
        DeviceVO vo = new DeviceVO(uuid, token, os, new Date(), rcvyn, appid, mbrid);
        try
        {
            int inserted = mService.insertDevice(vo);
            resp.setCode(ResponseBase.RESULT_OK);
            resp.setMessage(inserted + " Affected");
        }
        catch (Exception e)
        {
            resp.setCode(ResponseBase.RESULT_FAIL);
            resp.setMessage(e.getMessage());
        }
        return resp;
    }
    
    /**
     * 푸시 수신동의 여부 갱신
     * 
     * @param appid
     * @param rcvyn
     * @param uuid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/updateagr", method = RequestMethod.POST)
    @KeyRequired
    @ResponseBody
    public ResponseBase updateAgr(@RequestHeader(value = "appid") String appid, @RequestParam(value = "rcvyn") String rcvyn, @RequestParam("uuid") String uuid)
    {
        
        ResponseBase resp = new ResponseBase();
        try
        {
            int updated = mService.updateAgree(appid, uuid, rcvyn);
            resp.setCode(ResponseBase.RESULT_OK);
            resp.setMessage(updated + " Affected");
        }
        catch (Exception e)
        {
            resp.setCode(ResponseBase.RESULT_FAIL);
            resp.setMessage(e.getMessage());
        }
        
        return resp;
        
    }
    
    /**
     * 회원 정보 맵핑
     * 
     * @param appid
     * @param uuid
     * @param mbrid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/updatembr", method = RequestMethod.POST)
    @KeyRequired
    @ResponseBody
    public ResponseBase updateMbr(@RequestHeader(value = "appid") String appid, @RequestParam(value = "uuid") String uuid, @RequestParam("mbrid") String mbrid)
    {
        
        ResponseBase resp = new ResponseBase();
        try
        {
            int updated = mService.updateMember(appid, uuid, mbrid);
            resp.setCode(ResponseBase.RESULT_OK);
            resp.setMessage(updated + " Affected");
        }
        catch (Exception e)
        {
            resp.setCode(ResponseBase.RESULT_FAIL);
            resp.setMessage(e.getMessage());
        }
        
        return resp;
        
    }
    
    /**
     * 푸시 발송하기
     * 
     * @param appid
     * @param uuid
     * @param mbrid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @KeyRequired
    @ResponseBody
    public ResponseBase send(@RequestHeader(value = "appid") String appid, @RequestParam(value = "uuid", required = false) String uuid, @RequestParam(value = "mbrid", required = false) String mbrid, @RequestParam(value = "title") String title, @RequestParam(value = "body") String body,
            @RequestParam(value = "message") String message, @RequestParam(value = "image", required = false) String image, @RequestParam(value = "link", required = false) String link, @RequestParam(value = "targets", required = false) String[] targets,
            @RequestParam(value = "sound", defaultValue = "default") String sound)
    {
        
        ResponseBase resp = new ResponseBase();
        try
        {
            
            PayloadBase payload = null;
            
            if (targets == null)
            {
                payload = new TokenTargetPayload();
            }
            else
            {
                payload = new MulticastPayload();
            }
            
            PayloadBase.Notification notification = new PayloadBase.Notification();
            notification.body = body;
            notification.title = title;
            notification.sound = sound;
            
            PayloadBase.Data data = new PayloadBase.Data();
            data.image = image;
            data.link = link;
            
            payload.setData(data);
            payload.setNotification(notification);
            
            boolean result = mService.sendMessage(appid, uuid, mbrid, payload, targets);
            
            resp.setCode(ResponseBase.RESULT_OK);
            resp.setMessage("Sent::" + result);
            
        }
        catch (Exception e)
        {
            resp.setCode(ResponseBase.RESULT_FAIL);
            resp.setMessage(e.getMessage());
        }
        
        return resp;
    }
    
}
