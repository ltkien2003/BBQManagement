/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poly.kienltpc04639.assignment_final;

/**
 *
 * @author Kienltpc04639
 */
public class TaiKhoan {
    String tenDangNhap;
    String matKhau;
    String vaiTro;

    public TaiKhoan() {
    }

    public TaiKhoan(String tenDangNhap, String matKhau, String vaiTro) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
    }
    public String kiemTraVaiTro(String vt){
        return vt;
    }
}
