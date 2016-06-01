package org.demo.rule;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by barry.wong
 */
public class RulePlaceholder {

    private Map<String, Object> map;

    private Map<String, String> result;

    public RulePlaceholder(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public boolean getBoolean(String key) {
        return getMap().get(key) != null ? Boolean.parseBoolean(getMap().get(key).toString()) : false;
    }

    public String getString(String key) {
        return (String) getMap().get(key);
    }

    public BigDecimal getBigDecimal(String key) {

        BigDecimal value = null;

        if (getMap().get(key) != null) {
            if (getMap().get(key) instanceof BigDecimal) {
                value = (BigDecimal) getMap().get(key);
            } else {
                try {
                    value = new BigDecimal((String) getMap().get(key));
                } catch (NumberFormatException nfe) {
                    // if cannot parse, treat as null
                }
            }
        }

        return value;
    }

    public void put(String key, Object value) {
        getMap().put(key, value);
    }

    public Map<String, String> getResult() {
        if (result == null) {
            result = new TreeMap<>();
        }
        return result;
    }

    public String getResult(String key) {
        return getResult().get(key);
    }

    public void setResult(String key, String result) {
        getResult().put(key, result);
    }
}