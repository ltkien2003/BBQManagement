package com.poly.kienltpc04639.assignment_final;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.Serializable;

/**
 *
 * @author Kienltpc04639
 */
public class ThongTinMonAn implements Serializable{
    String maMonAn;
    String tenMonAn;
    int gia;
    String ngayCapNhat;
    int soLuong;
    String moTa;
    String iconHinhAnh;

    public ThongTinMonAn(String maMonAn, String tenMonAn, int gia, String ngayCapNhat, int soLuong, String moTa, String iconHinhAnh) {
        this.maMonAn = maMonAn;
        this.tenMonAn = tenMonAn;
        this.gia = gia;
        this.ngayCapNhat = ngayCapNhat;
        this.soLuong = soLuong;
        this.moTa = moTa;
        this.iconHinhAnh = iconHinhAnh;
    }




    
    
}
