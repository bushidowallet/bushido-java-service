package com.bitcoin.blockchain.api.doc;

import com.wordnik.swagger.config.DefaultSpecFilter;
import com.wordnik.swagger.model.ApiDescription;
import com.wordnik.swagger.model.Operation;
import com.wordnik.swagger.model.Parameter;

import java.util.List;
import java.util.Map;

/**
 * Created by Jesion on 2015-01-01.
 */
public class DocFilter extends DefaultSpecFilter
{
    @Override
    public boolean isOperationAllowed(Operation operation, ApiDescription apiDescription, Map<String, List<String>> stringListMap, Map<String, String> stringStringMap, Map<String, List<String>> stringListMap2) {
        return true;
    }

    @Override
    public boolean isParamAllowed(Parameter parameter, Operation operation, ApiDescription apiDescription, Map<String, List<String>> stringListMap, Map<String, String> stringStringMap, Map<String, List<String>> stringListMap2) {
        if( parameter.paramAccess().isDefined() && parameter.paramAccess().get().equals("internal") )
            return false;
        return true;
    }
}
