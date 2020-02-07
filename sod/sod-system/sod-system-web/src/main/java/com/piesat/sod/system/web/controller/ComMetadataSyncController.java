package com.piesat.sod.system.web.controller;
 /** 公共元数据同步Controller
*@description
*@author wlg
*@date 2020年2月6日下午5:24:31
*
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piesat.sod.system.rpc.api.ComMetadataSyncService;

@RestController
@RequestMapping(value="/restApi/comMetaData")
public class ComMetadataSyncController {
	
	@Autowired
	private ComMetadataSyncService comMetadataSyncService;

}
