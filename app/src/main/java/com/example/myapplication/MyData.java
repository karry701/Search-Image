package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyData {
    @SerializedName("data")
    private final List<Data> data;
    public MyData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }

    public static class Data {
        @SerializedName("title")
        private final String title;
        @SerializedName("images")
        private final List<Images> images;

        public Data(String title, List<Images> images) {
            this.title = title;
            this.images = images;
        }

        public String getTitle() {
            return title;
        }

        public List<Images> getImages() {
            return images;
        }

        public static class Images {
            @SerializedName("type")
            private final String type;
            @SerializedName("link")
            private final String link;

            public Images(String type, String link) {
                this.type = type;
                this.link = link;
            }

            public String getType() {
                return type;
            }
            public String getLink() {
                return link;
            }
        }
    }
}
