package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.managers.ContentManager;
import com.alodiga.services.provider.commons.models.Banner;
import com.alodiga.services.provider.commons.models.BannerType;
import com.alodiga.services.provider.commons.models.Language;
import com.alodiga.services.provider.web.utils.AccessControl;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import java.util.ArrayList;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;

public class SlideBannersViewController extends GenericForwardComposer {

    private static final long serialVersionUID = -9145887024839938515L;
    public static int countBanner = 0;
    private Div bannerDiv;
    private List<Banner> banners = new ArrayList<Banner>();
    private ContentManager contentManager;
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    public void initialize() {
        try {
            contentManager = ContentManager.getInstance();
            loadBanners();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadBanners() {
        try {

            if (AccessControl.getLanguage().equals(Language.ENGLISH)) {
                banners = contentManager.getBannersByTypeId(BannerType.HOME_CENTRAL_BANNER_USA_ENGLISH);
            } else {
                banners = contentManager.getBannersByTypeId(BannerType.HOME_CENTRAL_BANNER_USA_SPANISH);
            }

            int i = 0;
            for (Banner banner : banners) {
                try {
                    Image imgcentralBanner = new Image();
                    org.zkoss.image.AImage image = new org.zkoss.image.AImage(banner.getPath());
                    i++;
                    imgcentralBanner.setId("imgSlideShows_" + (i));
                    imgcentralBanner.setContent(image);
                    imgcentralBanner.setParent(bannerDiv);
                    imgcentralBanner.setWidth("980px");
                    imgcentralBanner.setHeight("320px");
                    if (i != 1) {
                        imgcentralBanner.setVisible(false);
                    }
                    if (banner.getUrl() != null && !banner.getUrl().isEmpty()) {
                        final String url = banner.getUrl();
                        imgcentralBanner.addEventListener("onClick", new EventListener() {

                            public void onEvent(Event event) throws Exception {
                                Executions.getCurrent().sendRedirect(url, "_blank");
                            }
                        });
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            countBanner = banners.size();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
     
    }
}
