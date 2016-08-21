package com.unravel.amanda.unravel.ravelryapi;

import java.util.ArrayList;

public class RavelryApiRequest {
    public ArrayList<String> requestParameters;
    public final RavelryApiCalls requestCommand;

    public RavelryApiRequest(String[] parameters, RavelryApiCalls requestCommand)
    {
        this.requestParameters = new ArrayList<>();
        for(String param:parameters)
        {
            requestParameters.add(param);
        }
        this.requestCommand = requestCommand;
    }

    public void addParameter(String parameter)
    {
        if(requestParameters == null)
        {
            requestParameters = new ArrayList<String>();
        }
        requestParameters.add(parameter);
    }
}
