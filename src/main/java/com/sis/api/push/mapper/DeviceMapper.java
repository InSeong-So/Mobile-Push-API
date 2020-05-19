package com.sis.api.push.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sis.api.push.model.AppInfoVO;
import com.sis.api.push.model.DeviceVO;

@Repository
@Mapper
public interface DeviceMapper
{
    
    List<DeviceVO> selectDevices(String appid);
    
    List<DeviceVO> selectDevice(String appid, String mbr_id);
    
    List<DeviceVO> selectDeviceByUUID(String appid, String uuid);
    
    AppInfoVO selectApiKey(String appid);
    
    int insertDevice(DeviceVO vo);
    
    int updateAgree(String appid, String uuid, String rcvyn);
    
    int updateMember(String appid, String uuid, String mbrid);
    
}
