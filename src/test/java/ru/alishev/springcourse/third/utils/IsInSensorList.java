package ru.alishev.springcourse.third.utils;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import ru.alishev.springcourse.third.model.Sensor;

import java.util.ArrayList;

public class IsInSensorList extends TypeSafeMatcher<String> {
    private ArrayList<Sensor> sensorList;

    public IsInSensorList(ArrayList<Sensor> sensorList) {
        this.sensorList = sensorList;
    }

    @Override
    protected boolean matchesSafely(String item) {
        for (Sensor sensor : sensorList) {
            if (sensor.getName().equals(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is one of the sensors in the sensorList");
    }
}