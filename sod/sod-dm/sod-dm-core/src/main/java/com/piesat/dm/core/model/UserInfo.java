package com.piesat.dm.core.model;

import com.piesat.dm.core.constants.Constants;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cwh
 * @date 2020年 12月04日 11:56:58
 */
@Data
public class UserInfo {
    private String userName;
    private String password;
    private List<String> whitelist;
    private String whitelistStr;

    public void setWhitelist(List<String> whitelist) {
        this.whitelist = whitelist;
        this.whitelistStr = whitelist.stream().collect(Collectors.joining(Constants.SPACE));
    }

    public void clearWhitelist() {
        this.whitelistStr = null;
    }
}
