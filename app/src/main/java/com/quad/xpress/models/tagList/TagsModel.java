package com.quad.xpress.models.tagList;

/**
 * Created by kural on 7/12/2016.
 */
public class TagsModel {
    String liked,file_id;
    private String tag,rank;
    public TagsModel(){

    }
    public TagsModel(String tag, String rank,String liked,String file_id){
        this.tag = tag;
        this.rank = rank;
        this.liked = liked;
        this.file_id =file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getFile_id() {
        return file_id;
    }

    public String getRank() {
        return rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLiked(){
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }
}
