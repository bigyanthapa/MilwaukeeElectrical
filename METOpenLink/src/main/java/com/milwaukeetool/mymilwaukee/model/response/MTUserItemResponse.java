package com.milwaukeetool.mymilwaukee.model.response;

import com.google.gson.annotations.SerializedName;
import com.milwaukeetool.mymilwaukee.model.MTSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott.hopfensperger on 12/9/2014.
 */
public class MTUserItemResponse {

    @SerializedName("sections")
    private ArrayList<MTSection> sections;

    public ArrayList<MTSection> getSections() {
        return sections;
    }

    public void setSections(ArrayList<MTSection> sections) {
        this.sections = sections;
    }

    public boolean isEmpty() {
        if (this.sections != null && !this.sections.isEmpty()) {
            for (MTSection section : this.sections) {
                if (section.containsItems()) {
                    return true;
                }
            }
        }

        return false;
    }
}
