package com.oyourneed.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.oyourneed.model.Cartdb;

import java.util.List;

@Dao
public interface AdminDao {

    @Query("SELECT * FROM product")
    List<Cartdb> getAllProduct();

    @Insert
    void insertProduct(Cartdb... notificationData);

    @Update
    void updateProduct(Cartdb... notificationData);

    @Query("SELECT * FROM product WHERE pro_id = :pro_id")
    Cartdb getSingleProduct(String pro_id);

    @Query("Delete FROM product WHERE pro_id = :pro_id")
    void getDeleteProduct(String pro_id);

    @Query("Delete FROM product")
    void getAllDeleteProduct();
}
