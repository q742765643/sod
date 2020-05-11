<template>
  <div class="app-container dbManagerTem">
    <!-- 数据库管理工具 -->
    <div class="bannerBox">
      <el-row>
        <el-col :span="18" style="padding-left:20px;">
          <h4>数据库可视化工具</h4>
          <p>以崭新的方式管理数据库，具备统一访问多个数据库的功能，能满足对关系型、非关系型数据库数据管理的需求。简约大方的界面，让您以崭新的方式访问数据库和操作数据。</p>
        </el-col>
        <el-col :span="6">
          <img src="@/views/systemHelp/dbManager/img/xuguBk.png" alt />
        </el-col>
      </el-row>
    </div>
    <section>
      <h4>虚谷</h4>
      <el-row>
        <el-col :span="8" class="imgBox">
          <img src="@/views/systemHelp/dbManager/img/xugu3.jpg" alt />
        </el-col>
        <el-col :span="16" class="textBox">
          <p class="iconText">
            <img src="@/views/systemHelp/dbManager/img/xicon1.png" alt />
            <span>分布式架构</span>
            <img src="@/views/systemHelp/dbManager/img/xicon2.png" alt />
            <span>通用关系型</span>
            <img src="@/views/systemHelp/dbManager/img/xicon3.png" alt />
            <span>高性能</span>
            <img src="@/views/systemHelp/dbManager/img/xicon4.png" alt />
            <span>高安全</span>
            <img src="@/views/systemHelp/dbManager/img/xicon5.png" alt />
            <span>高可靠</span>
          </p>
          <p
            class="detail"
          >主要解决信息数据的高效存贮、安全管理与快速检索等问题，主要功能包括数据字典、内存管理、事务管理、语法分析器、集成管理工具集等，提供数据定义、数据操作功能、数据库的运行管理、数据库的建立和维护功能。</p>
          <el-button
            size="small"
            type="success"
            icon="el-icon-download"
            @click="handleExport(xuguExportObj)"
          >客户端下载</el-button>
        </el-col>
      </el-row>
    </section>
    <section class="gray">
      <h4>南大通用</h4>
      <el-row>
        <el-col :span="16" class="textBox" style="padding-left:20px;">
          <p class="iconText">
            <img src="@/views/systemHelp/dbManager/img/xicon6.png" alt />
            <span>分析型</span>
            <img src="@/views/systemHelp/dbManager/img/xicon7.png" alt />
            <span>智能索引</span>
            <img src="@/views/systemHelp/dbManager/img/xicon8.png" alt />
            <span>提速</span>
            <img src="@/views/systemHelp/dbManager/img/xicon9.png" alt />
            <span>省盘</span>
            <img src="@/views/systemHelp/dbManager/img/xicon10.png" alt />
            <span>成云</span>
          </p>
          <p
            class="detail"
          >GBase 8a实现大数据的全数据（结构化数据、半结构化数据和非结构化数据）存储管理和高效分析，在百TB至PB级数据规模下实现数据查询的秒级响应；节省50%-90%存储空间；节省50%-90%的投资和运维成本；实现千亿级文本条目全文检索的秒级响应；提供全过程可视化的数据查询分析及展现工具。</p>
          <el-button
            size="small"
            type="success"
            icon="el-icon-download"
            @click="handleExport(GBaseExportObj)"
          >客户端下载</el-button>
          <el-button size="small" type="success" icon="el-icon-download" @click="webClick">WEB端浏览</el-button>
        </el-col>
        <el-col :span="8" class="imgBox">
          <img src="@/views/systemHelp/dbManager/img/xugu2.jpg" alt />
        </el-col>
      </el-row>
    </section>
    <section>
      <h4>CASSANDRA</h4>
      <el-row>
        <el-col :span="8" class="imgBox">
          <img src="@/views/systemHelp/dbManager/img/xgLibImage.png" alt />
        </el-col>
        <el-col :span="16" class="textBox">
          <p class="iconText">
            <img src="@/views/systemHelp/dbManager/img/xicon11.png" alt />
            <span>分布式结构化</span>
            <img src="@/views/systemHelp/dbManager/img/xicon12.png" alt />
            <span>刚性结构</span>
            <img src="@/views/systemHelp/dbManager/img/xicon13.png" alt />
            <span>网络服务</span>
            <img src="@/views/systemHelp/dbManager/img/xicon14.png" alt />
            <span>模式灵活</span>
            <img src="@/views/systemHelp/dbManager/img/xicon15.png" alt />
            <span>高可扩</span>
          </p>
          <p
            class="detail"
          >Cassandra是一种无性能损耗的高拓展性和可用性的分布式数据库。线性的可伸缩性以及被商品级硬件设施和云基础架构验证的容错技术使它成为任务关键型数据存储的良好平台。Cassandra支持跨多个数据中心的副本备份是非常棒的，为使用者提供低延迟和即使某个区域停电也能正常运转的放心。</p>
          <el-button
            size="small"
            type="success"
            icon="el-icon-download"
            @click="handleExport(CASSANDRAExportObj)"
          >客户端下载</el-button>
        </el-col>
      </el-row>
    </section>
  </div>
</template>

<script>
import { downloadFile } from "@/api/systemHelp/dbManager";
export default {
  data() {
    return {
      xuguExportObj: {
        name: "xugu-client"
      },
      GBaseExportObj: {
        name: "gbase-client"
      },
      webExportObj: {},
      CASSANDRAExportObj: {
        name: "cassandra-client"
      }
    };
  },
  created() {},
  methods: {
    handleExport(queryParam) {
      //显示延迟加载
      const loading = this.$loading({
        lock: true,
        text: "下载中，请稍候",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      downloadFile(queryParam).then(res => {
        this.downloadfileCommon(res);
        loading.close();
      });
    },
    webClick() {
      window.location.href = "http://10.40.17.54:8038/cmadaasmng/";
    }
  }
};
</script>
<style lang="scss">
.dbManagerTem {
  .bannerBox {
    width: 100%;
    height: 240px;
    background: -webkit-linear-gradient(left, #2980b9, #6dd5fa);
    background: -o-linear-gradient(right, #2980b9, #6dd5fa);
    background: -moz-linear-gradient(right, #2980b9, #6dd5fa);
    background: linear-gradient(to right, #2980b9, #6dd5fa);
    color: #fff;
    border-radius: 5px;
    .el-row {
      width: 1200px;
      margin: auto;
      h4 {
        font-size: 24px;
        margin-top: 14px;
        margin-top: 42px;
        font-weight: bold;
      }
      p {
        padding-right: 20px;
      }
      img {
        width: 100%;
        margin-top: 35px;
      }
    }
  }
  section {
    margin: 70px auto;
    .el-row {
      width: 1200px;
      margin: auto;
    }
    h4 {
      font-size: 28px;
      text-align: center;
      padding-bottom: 30px;
      font-weight: 100;
    }
    .imgBox {
      img {
        width: 100%;
      }
    }
    .textBox {
      font-size: 14px;
      padding: 25px 0 0 20px;
    }
    .iconText {
      margin-bottom: 10px;
      line-height: 26px;
      img {
        vertical-align: middle;
      }
    }
    .detail {
      line-height: 26px;
      color: #9b9ea0;
      margin-bottom: 14px;
    }
  }
  .gray {
    padding: 60px 0 70px 0;
    background-color: #f2f2f2;
    .textBox {
      font-size: 14px;
      padding: 40px 20px 0 0;
    }
  }
  .el-button--success {
    background-color: #1890ff;
    border-color: #1890ff;
  }
}
</style>
