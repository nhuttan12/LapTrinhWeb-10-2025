package com.example.Service.Admin;

import com.example.DAO.BannerDAO;
import com.example.DTO.Banners.GetBannerImagesPagingResponseDTO;
import com.example.DTO.Common.PagingResponse;
import com.example.Mappers.BannerMapper;
import com.example.Model.Banner;

import java.sql.SQLException;
import java.util.List;

public class BannerService {
    private final BannerDAO bannerDAO;
    private final BannerMapper bannerMapper;

    public BannerService(BannerDAO bannerDAO) {
        this.bannerDAO = bannerDAO;
        this.bannerMapper = BannerMapper.INSTANCE;
    }

    public PagingResponse<GetBannerImagesPagingResponseDTO> getBannersPaging(
            int page,
            int pageSize
    ) throws SQLException {
        // 1. Calculate offset
        int offset = (page - 1) * pageSize;

        PagingResponse<Banner> bannerImages = null;
        try {
            bannerImages = bannerDAO.getBannersPaging(
                    offset,
                    page,
                    pageSize);

            List<GetBannerImagesPagingResponseDTO> responseDTO = bannerMapper
                    .toGetBannerImagesPagingResponseDTOList(bannerImages.getItems());

            return PagingResponse.<GetBannerImagesPagingResponseDTO>builder()
                    .items(responseDTO)
                    .meta(bannerImages.getMeta())
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addBanner(String url) {
        try {
            return bannerDAO.addBanner(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteBanner(int id) {
        try {
            return bannerDAO.deleteBanner(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
