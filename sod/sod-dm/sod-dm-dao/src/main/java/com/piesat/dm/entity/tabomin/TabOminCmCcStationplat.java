package com.piesat.dm.entity.tabomin;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *  tab_omin_cm_cc_stationplat
 * @author 大狼狗 2020-05-18
 */
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_STATIONPLAT")
public class TabOminCmCcStationplat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_siteopf_id
     */
    @Column(name="c_siteopf_id",length = 36)
    private String cSiteopfId;

    /**
     * c_mdlang
     */
    @Column(name="c_mdlang",length = 50)
    private String cMdlang;

    /**
     * c_mdchar
     */
    @Column(name="c_mdchar",length = 20)
    private String cMdchar;

    /**
     * c_mddatest
     */
    @Column(name="c_mddatest",length = 20)
    private String cMddatest;

    /**
     * c_mdstanname
     */
    @Column(name="c_mdstanname",length = 60)
    private String cMdstanname;

    /**
     * c_mdstanver
     */
    @Column(name="c_mdstanver",length = 60)
    private String cMdstanver;

    /**
     * c_indexnbr
     */
    @Column(name="c_indexnbr",length = 32)
    private String cIndexnbr;

    /**
     * c_indexsubnbr
     */
    @Column(name="c_indexsubnbr",length = 32)
    private String cIndexsubnbr;

    /**
     * c_siteopf_flag
     */
    @Column(name="c_siteopf_flag",length = 32)
    private String cSiteopfFlag;

    /**
     * c_archiveinbr
     */
    @Column(name="c_archiveinbr",length = 32)
    private String cArchiveinbr;

    /**
     * c_stationnc
     */
    @Column(name="c_stationnc",length = 128)
    private String cStationnc;

    /**
     * c_stationne
     */
    @Column(name="c_stationne",length = 256)
    private String cStationne;

    /**
     * c_alias
     */
    @Column(name="c_alias",length = 128)
    private String cAlias;

    /**
     * c_siteopf_model
     */
    @Column(name="c_siteopf_model",length = 256)
    private String cSiteopfModel;

    /**
     * c_siteopf_type
     */
    @Column(name="c_siteopf_type",length = 36)
    private String cSiteopfType;

    /**
     * c_longitude
     */
    @Column(name="c_longitude",length = 32)
    private String cLongitude;

    /**
     * c_latitude
     */
    @Column(name="c_latitude",length = 32)
    private String cLatitude;

    /**
     * c_hp
     */
    @Column(name="c_hp",length = 32)
    private String cHp;

    /**
     * c_hha
     */
    @Column(name="c_hha",length = 32)
    private String cHha;

    /**
     * c_environment
     */
    @Column(name="c_environment",length = 256)
    private String cEnvironment;

    /**
     * c_regionid
     */
    @Column(name="c_regionid",length = 36)
    private String cRegionid;

    /**
     * c_timezone
     */
    @Column(name="c_timezone",length = 128)
    private String cTimezone;

    /**
     * c_climatezone
     */
    @Column(name="c_climatezone",length = 128)
    private String cClimatezone;

    /**
     * c_countryid
     */
    @Column(name="c_countryid",length = 32)
    private String cCountryid;

    /**
     * c_regionalhc
     */
    @Column(name="c_regionalhc",length = 32)
    private String cRegionalhc;

    /**
     * c_administrativedc
     */
    @Column(name="c_administrativedc",length = 32)
    private String cAdministrativedc;

    /**
     * c_address
     */
    @Column(name="c_address",length = 256)
    private String cAddress;

    /**
     * c_stationurl
     */
    @Column(name="c_stationurl",length = 256)
    private String cStationurl;

    /**
     * c_otherurl
     */
    @Column(name="c_otherurl",length = 256)
    private String cOtherurl;

    /**
     * c_estadate
     */
    @Column(name="c_estadate",length = 8)
    private String cEstadate;

    /**
     * c_repdate
     */
    @Column(name="c_repdate",length = 8)
    private String cRepdate;

    /**
     * c_cmethod
     */
    @Column(name="c_cmethod",length = 36)
    private String cCmethod;

    /**
     * c_status
     */
    @Column(name="c_status",length = 32)
    private String cStatus;

    /**
     * c_siteinformation
     */
    @Column(name="c_siteinformation",length = 32)
    private String cSiteinformation;

    /**
     * c_appendix
     */
    @Column(name="c_appendix",length = 256)
    private String cAppendix;

    /**
     * c_isexam
     */
    @Column(name="c_isexam",length = 10)
    private String cIsexam;

    /**
     * c_township
     */
    @Column(name="c_township",length = 100)
    private String cTownship;

    /**
     * c_organization
     */
    @Column(name="c_organization",length = 100)
    private String cOrganization;

    /**
     * c_continent
     */
    @Column(name="c_continent",length = 1)
    private String cContinent;

    /**
     * c_ocean
     */
    @Column(name="c_ocean",length = 1)
    private String cOcean;

    /**
     * c_longitude_numb
     */
    @Column(name="c_longitude_numb")
    private float cLongitudeNumb;

    /**
     * c_latitude_numb
     */
    @Column(name="c_latitude_numb")
    private float cLatitudeNumb;

    /**
     * c_manastation
     */
    @Column(name="c_manastation",length = 10)
    private String cManastation;

    /**
     * c_fundssources
     */
    @Column(name="c_fundssources",length = 100)
    private String cFundssources;

    /**
     * c_specifictype
     */
    @Column(name="c_specifictype",length = 100)
    private String cSpecifictype;

    /**
     * c_infofeedbackdep
     */
    @Column(name="c_infofeedbackdep",length = 100)
    private String cInfofeedbackdep;

    /**
     * c_moduleid
     */
    @Column(name="c_moduleid",length = 100)
    private String cModuleid;

    /**
     * c_windsensorheight
     */
    @Column(name="c_windsensorheight",length = 10)
    private String cWindsensorheight;

    /**
     * c_stationtype
     */
    @Column(name="c_stationtype",length = 10)
    private String cStationtype;

    /**
     * c_isuploadminutedata
     */
    @Column(name="c_isuploadminutedata",length = 10)
    private String cIsuploadminutedata;

    /**
     * c_isuploadhourdata
     */
    @Column(name="c_isuploadhourdata",length = 10)
    private String cIsuploadhourdata;

    /**
     * c_isuploaddaydata
     */
    @Column(name="c_isuploaddaydata",length = 10)
    private String cIsuploaddaydata;

    /**
     * c_isuploadsunshinedata
     */
    @Column(name="c_isuploadsunshinedata",length = 10)
    private String cIsuploadsunshinedata;

    /**
     * c_isuploadradiatehourdata
     */
    @Column(name="c_isuploadradiatehourdata",length = 10)
    private String cIsuploadradiatehourdata;

    /**
     * c_teldataprocesser
     */
    @Column(name="c_teldataprocesser",length = 256)
    private String cTeldataprocesser;

    /**
     * c_telmanager
     */
    @Column(name="c_telmanager",length = 50)
    private String cTelmanager;

    /**
     * c_remarks
     */
    @Column(name="c_remarks",length = 100)
    private String cRemarks;

    /**
     * c_short_name
     */
    @Column(name="c_short_name",length = 50)
    private String cShortName;

    /**
     * c_wind_sensor_height
     */
    @Column(name="c_wind_sensor_height",length = 50)
    private String cWindSensorHeight;

    /**
     * c_province_short
     */
    @Column(name="c_province_short",length = 50)
    private String cProvinceShort;

    /**
     * c_city_short
     */
    @Column(name="c_city_short",length = 50)
    private String cCityShort;

    /**
     * c_county_short
     */
    @Column(name="c_county_short",length = 50)
    private String cCountyShort;

    /**
     * c_equ_id
     */
    @Column(name="c_equ_id",length = 50)
    private String cEquId;

    /**
     * c_obg_num
     */
    @Column(name="c_obg_num",length = 50)
    private String cObgNum;

    /**
     * c_isuploadreghourdata
     */
    @Column(name="c_isuploadreghourdata",length = 10)
    private String cIsuploadreghourdata;

    /**
     * c_isuploadacidraindaydata
     */
    @Column(name="c_isuploadacidraindaydata",length = 50)
    private String cIsuploadacidraindaydata;

    /**
     * c_isweatherstation
     */
    @Column(name="c_isweatherstation",length = 50)
    private String cIsweatherstation;

    /**
     * c_dept
     */
    @Column(name="c_dept",length = 50)
    private String cDept;

    /**
     * c_if_mountain
     */
    @Column(name="c_if_mountain",length = 50)
    private String cIfMountain;

    /**
     * c_mdupdate
     */
    @Column(name="c_mdupdate",length = 8)
    private String cMdupdate;

    /**
     * c_istrafficstation
     */
    @Column(name="c_istrafficstation",length = 50)
    private String cIstrafficstation;

    /**
     * c_istouriststation
     */
    @Column(name="c_istouriststation",length = 50)
    private String cIstouriststation;

    /**
     * c_isfloodstation
     */
    @Column(name="c_isfloodstation",length = 10)
    private String cIsfloodstation;

    /**
     * c_create_date
     */
    @Column(name="c_create_date")
    private Date cCreateDate;

    /**
     * c_updated_date
     */
    @Column(name="c_updated_date")
    private Date cUpdatedDate;

    /**
     * c_modifier
     */
    @Column(name="c_modifier",length = 36)
    private String cModifier;

    /**
     * c_country
     */
    @Column(name="c_country",length = 36)
    private String cCountry;

    /**
     * c_country_name
     */
    @Column(name="c_country_name",length = 64)
    private String cCountryName;

    /**
     * c_city
     */
    @Column(name="c_city",length = 36)
    private String cCity;

    /**
     * c_city_name
     */
    @Column(name="c_city_name",length = 64)
    private String cCityName;

    /**
     * c_county
     */
    @Column(name="c_county",length = 36)
    private String cCounty;

    /**
     * c_county_name
     */
    @Column(name="c_county_name",length = 64)
    private String cCountyName;

    /**
     * c_chargeperson
     */
    @Column(name="c_chargeperson",length = 36)
    private String cChargeperson;

    /**
     * c_opt_type
     */
    @Column(name="c_opt_type",length = 8)
    private String cOptType;

    /**
     * c_raa
     */
    @Column(name="c_raa",length = 32)
    private String cRaa;

    /**
     * version
     */
    @ApiModelProperty("version")
    @Column(name="version")
    private Integer version;

    /**
     * c_countryname
     */
    @Column(name="c_countryname",length = 36)
    private String cCountryname;

    public TabOminCmCcStationplat() {
    }

}

