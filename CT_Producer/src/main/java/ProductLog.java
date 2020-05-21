import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProductLog {
    private List<String> phoneList = new ArrayList<String>();
    private Map<String, String> phoneNameMap = new HashMap<String, String>();

    private String startTime = "2020-01-01";
    private String endTime = "2020-12-30";

    private void initPhone() {
        phoneList.add("17078388295");
        phoneList.add("13980337439");
        phoneList.add("14575535933");
        phoneList.add("19902496992");
        phoneList.add("18549641558");
        phoneList.add("17005930322");
        phoneList.add("18468618874");
        phoneList.add("18576581848");
        phoneList.add("15978226424");
        phoneList.add("15542823911");
        phoneList.add("17526304161");
        phoneList.add("15422018558");
        phoneList.add("17269452013");
        phoneList.add("17764278604");
        phoneList.add("15711910344");
        phoneList.add("15714728273");
        phoneList.add("16061028454");
        phoneList.add("16264433631");
        phoneList.add("17601615878");
        phoneList.add("15897468949");

        phoneNameMap.put("17078388295", "李雁");
        phoneNameMap.put("13980337439", "卫艺");
        phoneNameMap.put("14575535933", "仰莉");
        phoneNameMap.put("19902496992", "陶欣悦");
        phoneNameMap.put("18549641558", "施梅梅");
        phoneNameMap.put("17005930322", "金虹霖");
        phoneNameMap.put("18468618874", "魏明艳");
        phoneNameMap.put("18576581848", "华贞");
        phoneNameMap.put("15978226424", "华啟倩");
        phoneNameMap.put("15542823911", "仲采绿");
        phoneNameMap.put("17526304161", "卫丹");
        phoneNameMap.put("15422018558", "戚丽红");
        phoneNameMap.put("17269452013", "何翠柔");
        phoneNameMap.put("17764278604", "钱溶艳");
        phoneNameMap.put("15711910344", "钱琳");
        phoneNameMap.put("15714728273", "缪静欣");
        phoneNameMap.put("16061028454", "焦秋菊");
        phoneNameMap.put("16264433631", "吕访琴");
        phoneNameMap.put("17601615878", "沈丹");
        phoneNameMap.put("15897468949", "褚美丽");
    }

    public static void main(String[] args) {

//        args = new String[]{"E:\\潭州课堂\\第八期\\ctlog.csv"};
        ProductLog productLog = new ProductLog();
        productLog.initPhone();
        productLog.writeLog(args[0]);

    }
    private void writeLog(String filePath){
        OutputStreamWriter outputStreamWriter = null;
        try {
             outputStreamWriter = new OutputStreamWriter(new FileOutputStream(filePath, true));
            while (true){


                String product = product();
                System.out.println(product);
                try {
                    outputStreamWriter.write(product + "\n");
                    outputStreamWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(outputStreamWriter != null){
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String product() {
        int callerIndex = (int) (Math.random() * phoneList.size());
        String caller = phoneList.get(callerIndex);
//        String callName = phoneNameMap.get(caller);

        int calleeIndex;
        while (true) {
            calleeIndex = (int) (Math.random() * phoneList.size());
            if (calleeIndex != callerIndex) {
                break;
            }
        }
        String callee = phoneList.get(calleeIndex);
//        String calleeName = phoneNameMap.get(caller);
        String buildTime = randomBuildTime(startTime, endTime);

        DecimalFormat df = new DecimalFormat("0000");
        String duration = df.format((int) (30 * 60 * Math.random()));
        StringBuilder sb = new StringBuilder();
        sb.append(caller + ",")
                .append(callee + ",")
                .append(buildTime + ",")
                .append(duration);
        return sb.toString();
    }

    private String randomBuildTime(String startTime, String endTime) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = sdf1.parse(startTime);
            Date endDate = sdf1.parse(endTime);

            if (endDate.getTime() <= startDate.getTime()) return null;
            long time = startDate.getTime() + (long) ((endDate.getTime() - startDate.getTime()) * Math.random());

            return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
