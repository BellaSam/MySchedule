package com.pintn.www.myschedule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pintn.www.myschedule.entity.City;
import com.pintn.www.myschedule.weathertask.CityListTask;
import com.pintn.www.myschedule.weathertask.RecentWeatherTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private TextView weatherDetailLabel ;
    private EditText cityName;
    private Spinner citySpinner;
    private Spinner areaSpinner;

    private Map<String,City> cityMap = new HashMap<>();
    private boolean isCitySpinnerFirst = true;
    private boolean isAreaSpinnerFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherDetailLabel  = (TextView) this.findViewById(R.id.weatherDetail);
        cityName = (EditText) this.findViewById(R.id.cityName);
        citySpinner = (Spinner) this.findViewById(R.id.cityList);
        areaSpinner = (Spinner) this.findViewById(R.id.areaList);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if(isCitySpinnerFirst){
                    view.setVisibility(View.INVISIBLE);
                }
                isCitySpinnerFirst = false;

                String selectedCity = (String) citySpinner.getSelectedItem();
                List<String> list = new ArrayList<String>();
                try {
                    String result = new CityListTask().execute(selectedCity).get();
                    JSONObject jsonStr = new JSONObject(result);
                    int errNum = jsonStr.getInt("errNum");
                    String errMsg = jsonStr.getString("errMsg");
                    System.out.println("errNum = [" + errNum + "]");
                    System.out.println("errMsg = [" + errMsg + "]");

                    List<City> areaList = new ArrayList<>();
                    JSONArray array = jsonStr.getJSONArray("retData");
                    for(int i=1;i<array.length();i++){
                        JSONObject json = (JSONObject) array.get(i);
                        City area = new City();
                        area.setId(json.getString("area_id"));
                        area.setProvinceCN(json.getString("province_cn"));
                        area.setDistrictCN(json.getString("district_cn"));
                        area.setNameCN(json.getString("name_cn"));
                        area.setNameEn(json.getString("name_en"));
                        areaList.add(area);
                        cityMap.put(area.getNameCN(),area);
                    }
                    System.out.println("area list : " + areaList.size());

                    List<String> areaNameList = getSortedCityNameList(areaList);
                    ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,areaNameList);
                    areaSpinner.setAdapter(areaAdapter);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if(isAreaSpinnerFirst){
                    view.setVisibility(View.INVISIBLE);
                }
                isAreaSpinnerFirst = false;

                String areaName = (String) areaSpinner.getSelectedItem();
                City area = cityMap.get(areaName);
                System.out.println("area : " + area.toString());

                StringBuilder status = new StringBuilder();
                try {
                    String result = new RecentWeatherTask().execute(area.getNameCN(),area.getId()).get();
                    JSONObject jsonStr = new JSONObject(result);
                    System.out.println("errNum : "+jsonStr.getString("errNum"));
                    System.out.println("errMsg : "+jsonStr.getString("errMsg"));

                    JSONObject retData = jsonStr.getJSONObject("retData");
                    String city = retData.getString("city");
                    String cityId = retData.getString("cityid");
                    System.out.println("city : "+ city + ", cityid : " + cityId);

                    JSONObject today = retData.getJSONObject("today");
                    String dateStr = today.getString("date");
                    String weekday = today.getString("week");
                    String curTemp = today.getString("curTemp");
                    String aqi = today.getString("aqi");
                    String fengxiang = today.getString("fengxiang");
                    String fengli = today.getString("fengli");
                    String hightemp = today.getString("hightemp");
                    String lowtemp = today.getString("lowtemp");
                    String weatherType = today.getString("type");
                    status.append("城市: ").append(city).append("\n")
                            .append("日期: ").append(dateStr).append("  ").append(weekday).append("\n")
                            .append("当前温度: ").append(curTemp).append("  ").append("PM2.5: ").append(aqi).append("\n")
                            .append("风向: ").append(fengxiang).append("  ").append("风力: ").append(fengli).append("  ").append(weatherType).append("\n")
                            .append("最高温度: ").append(hightemp).append("  ").append("最低温度: ").append(lowtemp).append("\n");

                    JSONArray lifeStatusList = today.getJSONArray("index");
                    if(lifeStatusList != null && lifeStatusList.length()>0){
                        for(int i=0;i<lifeStatusList.length();i++){
                            JSONObject lifeStatusJson = lifeStatusList.getJSONObject(i);
                            status.append(lifeStatusJson.getString("name")).append(" : ").append(lifeStatusJson.getString("index")).append("\n")
                                    .append(lifeStatusJson.getString("details")).append("\n");
                        }
                    }

                    weatherDetailLabel.setText(status.toString());
                    System.out.println("weather result : " + result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public List<String> getSortedCityNameList(List<City> cityList){
        List<String> sortedList = new ArrayList<>();

        Collections.sort(cityList, new Comparator<City>() {
            @Override
            public int compare(City c1, City c2) {
                return c1.getNameEn().compareTo(c2.getNameEn());
            }
        });

        for(City city : cityList){
            sortedList.add(city.getNameCN());
            System.out.println("city : " + city.toString());
        }

        return sortedList;
    }

}
