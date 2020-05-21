package com.zyd.people;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PeopleModel implements Writable {
    private int id;
    private String gender;
    private int hight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHight() {
        return hight;
    }

    public void setHight(int hight) {
        this.hight = hight;
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(getId());
        dataOutput.writeUTF(getGender());
        dataOutput.writeInt(getHight());
    }

    public void readFields(DataInput dataInput) throws IOException {
        setId(dataInput.readInt());
        setGender(dataInput.readUTF());
        setHight(dataInput.readInt());
    }
}
