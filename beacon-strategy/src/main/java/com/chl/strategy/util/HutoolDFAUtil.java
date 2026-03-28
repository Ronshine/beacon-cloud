package com.chl.strategy.util;

import cn.hutool.dfa.WordTree;
import com.chl.common.constant.ApiConstants;
import com.chl.strategy.client.BeaconCacheClient;

import java.util.List;
import java.util.Set;

public class HutoolDFAUtil {
    /**
     * 构建hutool需要存放敏感词树的类
     */
    private static WordTree wordTree = new WordTree();

    static {
        BeaconCacheClient cacheClient = (BeaconCacheClient) SpringUtil.getBeanByClass(BeaconCacheClient.class);
        Set<String> dirtyWords  = cacheClient.sMembers(ApiConstants.DIRTY_WORD);
        wordTree.addWords(dirtyWords);
    }

    public static List<String> matchAll(String text){
        return wordTree.matchAll(text);
    }
}
