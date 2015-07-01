package com.example.commonframe.util;

import android.content.Context;
import android.os.Environment;

import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class LocalReporter implements ReportSender {

    private final Map<ReportField, String> reportMap = new HashMap<ReportField, String>();
    private FileWriter crashReport = null;

    public LocalReporter() {
        File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/" + CentralApplication.getContext().getPackageName().replace(".", "_"));
        if (!directory.exists())
            if (directory.mkdir()) {
                File log = new File(directory.getPath(), "log.txt");
                try {
                    crashReport = new FileWriter(log, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    private Map<String, String> refactor(Map<ReportField, String> report) {

        ReportField[] fields = ACRA.getConfig().customReportContent();
        if (fields.length == 0) {
            fields = ACRAConstants.DEFAULT_REPORT_FIELDS;
        }

        final Map<String, String> result = new HashMap<String, String>(
                report.size());
        for (ReportField field : fields) {
            if (reportMap == null || reportMap.get(field) == null) {
                result.put(field.toString(), report.get(field));
            } else {
                result.put(reportMap.get(field), report.get(field));
            }
        }
        return result;
    }

    @Override
    public void send(Context context, CrashReportData errorContent) throws ReportSenderException {
        final Map<String, String> finalReport = refactor(errorContent);

        try {
            BufferedWriter buffer = new BufferedWriter(crashReport);

            Set<Entry<String, String>> set = finalReport.entrySet();
            for (Entry<String, String> entry : set) {
                buffer.append("[" + entry.getKey() + "]=" + entry.getValue());
            }
            buffer.flush();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}