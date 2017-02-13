package org.wso2.carbon.testjar.mgt;


import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.analytics.api.AnalyticsDataAPI;
import org.wso2.carbon.analytics.datasource.commons.Record;
import org.wso2.carbon.analytics.jsservice.AnalyticsJSServiceConnector;
import org.wso2.carbon.analytics.jsservice.beans.ResponseBean;
import org.wso2.carbon.analytics.jsservice.exception.JSServiceException;
import org.wso2.carbon.analytics.jsservice.internal.ServiceHolder;
import org.wso2.carbon.event.stream.core.EventStreamService;
import org.wso2.carbon.testjar.mgt.beans.ColumnKeyValueBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PutAnalyticsRecord extends AnalyticsJSServiceConnector {

    private Log logger = LogFactory.getLog(AnalyticsJSServiceConnector.class);
    private AnalyticsDataAPI analyticsDataAPI = ServiceHolder.getAnalyticsDataAPI();
    private EventStreamService eventStreamService = ServiceHolder.getEventStreamService();
    private Gson gson = new Gson();

    public PutAnalyticsRecord() {
    }

    public ResponseBean put(String username, String tableName, String valuesBatch) {
        if(this.logger.isDebugEnabled()) {
            this.logger.debug("Invoking getRecordByRange for tableName: " + tableName);
        }

        try {
            if(valuesBatch != null) {
                ColumnKeyValueBean columnKeyValueBean = (ColumnKeyValueBean)this.gson.fromJson(valuesBatch, ColumnKeyValueBean.class);
                Map valueBatchList = columnKeyValueBean.getColumns();
                //int tenantId = java.lang.Integer.parseInt(username);
                Record oneRecord = new Record(-1234,tableName,valueBatchList);
                List<Record> list = new ArrayList();
                list.add(oneRecord);
                if(valueBatchList != null && !valueBatchList.isEmpty()) {
                    this.analyticsDataAPI.put(list);

                    return this.handleResponse(PutAnalyticsRecord.ResponseStatus.SUCCESS, "Successfully put records to table:"+ tableName);
                } else {
                    throw new JSServiceException("Values batch is null or empty");
                }
            } else {
                throw new JSServiceException("Values batch is not provided");
            }
        } catch (Exception var8) {
            this.logger.error("failed to put records to table: \'" + tableName + "\', " + var8.getMessage(), var8);
            return this.handleResponse(PutAnalyticsRecord.ResponseStatus.FAILED, "Failed to put records to table: \'" + tableName + "\', " + var8.getMessage());
        }
    }

    public ResponseBean clearInexData(String username, String tableName) {
        if(this.logger.isDebugEnabled()) {
            this.logger.debug("Invoking clearIndexData for tableName : " + tableName);
        }

        try {
            this.analyticsDataAPI.clearIndexData(username, tableName);
            return this.handleResponse(PutAnalyticsRecord.ResponseStatus.SUCCESS, "Successfully cleared indices in table: " + tableName);
        } catch (Exception var4) {
            this.logger.error("Failed to clear indices for table: " + tableName + ": " + var4.getMessage());
            return this.handleResponse(PutAnalyticsRecord.ResponseStatus.FAILED, "Failed to clear indices for table: " + tableName + ": " + var4.getMessage());
        }
    }
    public ResponseBean getTableLis(String username) {
        Object tableList;
        try {
            tableList = this.analyticsDataAPI.listTables(username);
        } catch (Exception var4) {
            this.logger.error("Unable to get table list:" + var4.getMessage(), var4);
            return this.handleResponse(AnalyticsJSServiceConnector.ResponseStatus.FAILED, "Unable to get table list: " + var4.getMessage());
        }

        if(tableList == null || ((List)tableList).isEmpty()) {
            if(this.logger.isDebugEnabled()) {
                this.logger.debug("Received an empty table name list!");
            }

            tableList = new ArrayList();
        }
        return this.handleResponse(AnalyticsJSServiceConnector.ResponseStatus.SUCCESS, this.gson.toJson(tableList));
    }

}
