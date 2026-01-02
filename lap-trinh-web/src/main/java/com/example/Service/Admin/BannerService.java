package com.example.Service.Admin;
import com.example.DAO.BannerDAO;
import com.example.Model.Image;

import java.sql.SQLException;
import java.util.List;
public class BannerService {
    private final BannerDAO bannerDAO;

    public BannerService(BannerDAO bannerDAO) {
        this.bannerDAO = bannerDAO;
    }

    public List<Image> getAllBanners() throws SQLException {
        return bannerDAO.getAllBanners();
    }

    public boolean addBanner(String url) throws SQLException {
        return bannerDAO.addBanner(url);
    }

    public boolean deleteBanner(int id) throws SQLException {
        return bannerDAO.deleteBanner(id);
    }
}
