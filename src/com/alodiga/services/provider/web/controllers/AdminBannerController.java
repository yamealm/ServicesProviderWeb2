package com.alodiga.services.provider.web.controllers;
import com.alodiga.services.provider.commons.ejbs.BannerEJB;
//import com.alodiga.services.provider.commons.managers.ContentManager;
import com.alodiga.services.provider.commons.models.Banner;
import com.alodiga.services.provider.commons.models.BannerType;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.Utils;
import com.alodiga.services.provider.web.utils.WebConstants;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import org.zkoss.util.resource.Labels;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

public class AdminBannerController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtName;
    private Textbox txtUrl;
    private Textbox txtAlt;
    private Textbox txtTitle;
    private Textbox txtDescription;
    private Intbox intOrder;
    private Vbox divPreview;
    private Combobox cmbBannerType;
    private Checkbox cbxEnabled;
    private BannerEJB bannerEJB = null;
    private Banner bannerParam;
    private Button btnUpload;
    private Button btnSave;
    private Button btnBack;
    private Media mediaFile = null;
    private boolean uploaded = false;
    String format = "";

    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        bannerParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Banner) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        initView(eventType, "sp.crud.banner");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.banner");
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            bannerEJB = (BannerEJB) EJBServiceLocator.getInstance().get(EjbConstants.BANNER_EJB);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadBannerTypes(BannerType bannerType) {
                try {
            List<BannerType> bannerTypes = bannerEJB.getBannerTypes();

            for (int i = 0; i < bannerTypes.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(bannerTypes.get(i));
                item.setLabel(bannerTypes.get(i).getName());
                item.setParent(cmbBannerType);
                if (bannerType != null && bannerTypes.get(i).getId().equals(bannerType.getId())) {
                    cmbBannerType.setSelectedItem(item);
                }
            }
        } catch (Exception ex) {
             showError(ex);
        }
    }

    public void clearFields() {
        txtName.setRawValue(null);
        txtUrl.setRawValue(null);
        txtAlt.setRawValue(null);
        txtTitle.setRawValue(null);
        txtDescription.setRawValue(null);
        intOrder.setRawValue(null);
        cbxEnabled.setChecked(true);
        btnUpload.setDisabled(true);
    }

    private void loadFields(Banner banner) {
        
            txtName.setText(banner.getName());
            txtUrl.setText(banner.getUrl());
            txtTitle.setText(banner.getTitle());
            txtAlt.setText(banner.getAlt());
            txtDescription.setText(banner.getDescription());
            cbxEnabled.setChecked(banner.isEnabled());
            intOrder.setValue(banner.getOrderList());
            List<Comboitem> items = (List<Comboitem>) cmbBannerType.getItems();

        for (Comboitem item : items) {
            Long id = (Long) item.getValue();
            if (id.equals(banner.getBannerType().getId())) {
                cmbBannerType.setSelectedItem(item);
            }
        }
        try {
            AImage image;
            image = new org.zkoss.image.AImage(banner.getPath());
            org.zkoss.zul.Image imageFile = new org.zkoss.zul.Image();
            imageFile.setContent(image);
            //imageFile.setWidth("90%");
            imageFile.setParent(divPreview);
        } catch (Exception e) {
            e.printStackTrace();
      }
 }
     public void blockFields() {
        txtName.setReadonly(true);
        txtUrl.setReadonly(true);
        txtAlt.setReadonly(true);
        txtTitle.setReadonly(true);
        txtDescription.setReadonly(true);
        cbxEnabled.setDisabled(true);
        intOrder.setReadonly(true);
        btnSave.setVisible(false);
        btnUpload.setDisabled(true);
    }

    public Boolean validateEmpty() {
        boolean valid = false;
        if (cmbBannerType.getSelectedIndex() == -1) {
            cmbBannerType.setFocus(true);
            this.showMessage("sp.error.field.required", true, null);
        } else if (txtName.getText().isEmpty()) {
            txtName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtTitle.getText().isEmpty()) {
            txtTitle.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (intOrder.getValue() == null || intOrder.getValue() <= 0) {
            intOrder.setFocus(true);
            this.showMessage("sp.error.invalid.order.number", true, null);
        } else if (bannerParam == null && mediaFile == null) {
            this.showMessage("sp.error.field.required", true, null);
        } else {
            valid = true;
        }
        return valid;

    }

    private void saveBanner(Banner _banner ) {

         Banner banner = null;
        try {
             if (_banner != null) {
                banner = _banner;
            } else {
                banner = new Banner();
            }
            banner.setName(txtName.getText());
            banner.setUrl(txtUrl.getText());
            banner.setAlt(txtAlt.getText());
            banner.setTitle(txtTitle.getText());
            banner.setDescription(txtDescription.getText());
            banner.setEnabled(cbxEnabled.isChecked());
            banner.setOrderList(intOrder.getValue());
//            BannerType bannerType = new BannerType();
//            bannerType.setId((Long) cmbBannerType.getSelectedItem().getValue());
            banner.setBannerType((BannerType) cmbBannerType.getSelectedItem().getValue());
            String fileName = "" + Calendar.getInstance().getTimeInMillis();
            if (_banner != null) {
                if (uploaded) {
                    banner.setPath(WebConstants.BANNER_FILE_PACH + fileName + "." + format);
                    BufferedInputStream in = null;
                    BufferedOutputStream out = null;
                    try {
                        InputStream fin = mediaFile.getStreamData();
                        in = new BufferedInputStream(fin);
                        File baseDir = new File(WebConstants.BANNER_FILE_PACH);
                        if (!baseDir.exists()) {
                            baseDir.mkdirs();
                            baseDir.setWritable(true);
                            baseDir.setReadable(true);
                            baseDir.setExecutable(true);
                        }
                        File file = new File(WebConstants.BANNER_FILE_PACH + fileName + "." + format);
                        OutputStream fout = new FileOutputStream(file);
                        out = new BufferedOutputStream(fout);
                        byte buffer[] = new byte[1024];
                        int ch = in.read(buffer);
                        while (ch != -1) {
                            out.write(buffer, 0, ch);
                            ch = in.read(buffer);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }

                            if (in != null) {
                                in.close();
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }

            } else {

                BufferedInputStream in = null;
                BufferedOutputStream out = null;
                try {
                    InputStream fin = mediaFile.getStreamData();
                    in = new BufferedInputStream(fin);
                    File baseDir = new File(WebConstants.BANNER_FILE_PACH);
                    if (!baseDir.exists()) {
                        baseDir.mkdirs();
                        baseDir.setWritable(true);
                        baseDir.setReadable(true);
                        baseDir.setExecutable(true);
                    }
                    File file = new File(WebConstants.BANNER_FILE_PACH + fileName + "." + format);
                    OutputStream fout = new FileOutputStream(file);
                    out = new BufferedOutputStream(fout);
                    byte buffer[] = new byte[1024];
                    int ch = in.read(buffer);
                    while (ch != -1) {
                        out.write(buffer, 0, ch);
                        ch = in.read(buffer);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }

                        if (in != null) {
                            in.close();
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                banner.setPath(WebConstants.BANNER_FILE_PACH + fileName + "." + format);
            }
            request.setParam(banner);
            banner = bannerEJB.saveBanner(request);
            bannerParam = banner;
            this.showMessage("sp.common.save.success", false, null);
        } catch (Exception e) {
            e.printStackTrace();
            this.showMessage("sp.error.general", true, null);
           }

    }

    public void onUpload$btnUpload(org.zkoss.zk.ui.event.UploadEvent event) throws Throwable {


        if (event.getMedia() != null) {
            divPreview.getChildren().clear();
            mediaFile = event.getMedia();
            format = mediaFile.getFormat();
            org.zkoss.zul.Image image = new org.zkoss.zul.Image();
            image.setContent((org.zkoss.image.Image) mediaFile);
            image.setWidth("250px");
            image.setParent(divPreview);
            uploaded = true;
        }
    }
    
      public void onClick$btnSave() {
        if (validateEmpty()) {

            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    saveBanner(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveBanner(bannerParam);
                    break;
                default:
                    break;
            }
           
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                btnBack.setVisible(true);
                btnSave.setVisible(true);
                btnUpload.setVisible(true);
                loadFields(bannerParam);
                loadBannerTypes(bannerParam.getBannerType());
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(bannerParam);
                loadBannerTypes(bannerParam.getBannerType());
                btnBack.setVisible(true);
                blockFields();
                break;
            case WebConstants.EVENT_ADD:
                loadBannerTypes(null);
                btnBack.setVisible(true);
                btnSave.setVisible(true);
                btnUpload.setVisible(true);
                break;
            default:
                break;
        }
     }
}

