package com.chl.strategy.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * DFA算法实现敏感词树的构建以及提取出敏感词
 */
public class DFAUtil {
    private static Map hfaMap = new HashMap<>();

    private static final String IS_END = "isEnd";

    public static void main(String[] args) {
        Set<String> dirtyWords = new HashSet<>();
        dirtyWords.add("傻逼");
        dirtyWords.add("傻子");
        dirtyWords.add("卧槽你妈");
        dirtyWords.add("你妈");

        create(dirtyWords);

        for (Object o : hfaMap.entrySet()) {
            System.out.println(o);
        }

        String text = "卧槽你妈你是个傻子还是个傻逼啊";

        Set<String> dirtyWordSet = getDirtyWordMap(text);
        for (String dirtyWord : dirtyWordSet) {
            System.out.println(dirtyWord);
        }

    }

    /**
     * 再确定是敏感词的时候构建敏感词树
     * @param dirtyWords 传入的敏感词集合
     */
    public static void create(Set<String> dirtyWords){
        //声明初始化临时map
        Map nowMap;

        //对敏感词集合进行遍历取出
        for (String dirtyWord : dirtyWords) {
            //将节点指向根节点
            nowMap = hfaMap;
            //对取出的词进行遍历保存
            for (int i = 0; i < dirtyWord.length(); i++) {
                //判断集合中是否有当前这个字
                String word = String.valueOf(dirtyWord.charAt(i));
                Map map = (Map) nowMap.get(word);
                if (map == null){
                    //当前没有这个字将其存入当前节点
                    map = new HashMap();
                    nowMap.put(word,map);
                }
                nowMap = map;

                //如果当前这个字具有isEnd或者值为1，不做处理直接跳出
                if (nowMap.containsKey(IS_END) && nowMap.get(IS_END).equals("1")){
                    continue;
                }

                //如果没有isEnd这个值
                if (i == dirtyWord.length() - 1){
                    //是最后一个字就将它设置为1
                    nowMap.put(IS_END,"1");
                }else {
                    //压根就没有idEnd或者不是最后一个字就设置为0
                    nowMap.put(IS_END,"0");
                }
            }
        }
    }

    /**
     * 基于敏感词树对传入文本判断出其中的敏感词并返回
     * @param text 输入的文本信息
     * @return 返回文本中匹配的敏感词
     */
    public static Set<String> getDirtyWordMap(String text){
        //创建返回的集合
        Set<String> dirtyWords =  new HashSet<>();

        //对传入的文本进行遍历
        for (int i = 0; i < text.length(); i++) {
            //创建两个对应存储索引位置
            int nextLength = 0;
            int dirtyLength = 0;

            //创建临时存储map
            Map nowMap = hfaMap;

            //再进行循环进行判断
            for (int j = i; j < text.length(); j++) {
                String word = String.valueOf(text.charAt(j));
                nowMap = (Map) nowMap.get(word);
                if (nowMap == null){
                    //证明没有这个字开头的敏感词跳出循环
                    break;
                }else {
                    dirtyLength++;
                    //有这个字对它的进行查询看是否这个字就是敏感词
                    if ("1".equals(nowMap.get(IS_END))){
                        nextLength = dirtyLength;
                        break;
                    }
                }
            }
            //如果nextLength大于0证明匹配到了敏感词
            if (nextLength > 0){
                //对信息文本进行截取存入集合中
                dirtyWords.add(text.substring(i,i + nextLength));
                //因为进行下次遍历时i会自动++所以需要再进入前-1
                i = i + nextLength - 1;
            }
        }
        return dirtyWords;
    }
}
