package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> findByUserId(Integer userId);

    @Select("SELECT * FROM FILES WHERE filename = #{filename} AND userid = #{userId}")
    File findByFilenameAndUserId(String filename, Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, filedata, userid) VALUES(#{filename}, #{contentType}, #{fileSize}, #{fileData}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    void delete(Integer fileId);


    @Select("SELECT * FROM FILES WHERE fileId = #{fileId} AND userId = #{userId}")
    File getFile(Integer fileId, Integer userId);

}
