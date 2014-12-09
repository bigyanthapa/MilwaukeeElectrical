package com.milwaukeetool.mymilwaukee.model.response;

import com.google.gson.annotations.SerializedName;
import com.milwaukeetool.mymilwaukee.model.MTSection;

import java.util.List;

/**
 * Created by scott.hopfensperger on 12/9/2014.
 */
public class MTUserItemResponse {

    @SerializedName("sections")
    private List<MTSection> sections;

    public List<MTSection> getSections() {
        return sections;
    }

    public void setSections(List<MTSection> sections) {
        this.sections = sections;
    }

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }
}
