<template>
  <div class="app-container">
    <div class="businessVue">
      <h5 class="title">业务场景说明</h5>
      <el-row class="sectionTop">
        <el-col :span="6" class="inline-item">
          <img src="@/views/systemHelp/bussiness/img/b1.png" class="topImg" />
          <p class="imgtips">新增资料维护</p>
        </el-col>
        <el-col :span="6" class="inline-item">
          <img src="@/views/systemHelp/bussiness/img/b2.png" class="topImg" />
          <p class="imgtips">已有资料管理</p>
        </el-col>
        <el-col :span="6" class="inline-item">
          <img src="@/views/systemHelp/bussiness/img/b3.png" class="topImg" />
          <p class="imgtips">用户资料访问</p>
        </el-col>
        <el-col :span="6" class="inline-item">
          <img src="@/views/systemHelp/bussiness/img/b1.png" class="topImg" />
          <p class="imgtips">存储资源使用</p>
        </el-col>
      </el-row>
      <h5 class="title" id="pageFlag0">新增资料维护业务流程</h5>
      <div class="stepBox">
        <p class="stepTitle">
          <i class="fa fa-bookmark"></i>
          申请流程
        </p>
        <ul class="stepUl">
          <li>
            <span>
              <i class="fa fa-file"></i> 资料申请
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-address-book-o"></i> 存储结构管理
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-retweet"></i> 数据同步配置
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-database"></i> 数据迁移与清除配置
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-files-o"></i> 数据备份与恢复配置
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-clipboard"></i> 数据归档配置
            </span>
          </li>
        </ul>
      </div>
      <el-card class="box-card">
        <el-row>
          <el-col :span="14" class="textBox">
            <p>用户提交资料注册申请:管理员在数据存储管理系统中进行资料存储结构定义，并进行数据同步配置、数据迁移与清除配置、数据备份配置和数据归档配置。最后通知用户可以上传资料。</p>
          </el-col>
          <el-col :span="10" class="imgBox">
            <img src="@/assets/home/images/banner1.gif" alt />
          </el-col>
        </el-row>
      </el-card>
      <h5 class="title" id="pageFlag1">已有资料信息管理业务流程</h5>
      <div class="stepBox">
        <p class="stepTitle">
          <i class="fa fa-bookmark"></i>
          申请流程
        </p>
        <ul class="stepUl">
          <li>
            <span>
              <i class="fa fa-file"></i> 资料信息概览
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-address-book-o"></i> 存储结构管理
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-retweet"></i> 数据同步配置
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-database"></i> 数据迁移与清除配置
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-files-o"></i> 数据备份与恢复配置
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-clipboard"></i> 数据归档配置
            </span>
          </li>
        </ul>
      </div>
      <el-card class="box-card">
        <el-row>
          <el-col :span="10" class="imgBox">
            <img src="@/assets/home/images/banner2.gif" alt />
          </el-col>
          <el-col :span="14" class="textBox">
            <p
              style="padding-left:20px;"
            >通过存储管理系统的资料信息概览页面，查看所有已有资料的表结构信息及表结构、同步、迁移、清除、备份恢复等功能是否配置、是否需要配置信息； 点击相应的功能键，弹出各个功能点的配置框，进行配置信息的修改和配置。</p>
          </el-col>
        </el-row>
      </el-card>
      <h5 class="title" id="pageFlag2">用户资料访问业务流程</h5>
      <div class="stepBox">
        <p class="stepTitle">
          <i class="fa fa-bookmark"></i>
          申请流程
        </p>
        <ul class="stepUl stepUl2">
          <li>
            <span>
              <i class="fa fa-file"></i> 用户数据库访问账户申请
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-address-book-o"></i> 管理员审核创建数据库访问账户
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-retweet"></i> 用户提交资料访问权限申请
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-database"></i> 管理员审核分配资料访问权限
            </span>
          </li>
        </ul>
      </div>
      <el-card class="box-card">
        <el-row>
          <el-col :span="14" class="textBox">
            <p>用户提交资料注册申请:管理员在数据存储管理系统中进行资料存储结构定义，并进行数据同步配置、数据迁移与清除配置、数据备份配置和数据归档配置。最后通知用户可以上传资料。</p>
          </el-col>
          <el-col :span="10" class="imgBox">
            <img src="@/assets/home/images/banner3.gif" alt />
          </el-col>
        </el-row>
      </el-card>
      <h5 class="title" id="pageFlag3">用户存储资源使用业务流程</h5>
      <div class="stepBox">
        <p class="stepTitle">
          <i class="fa fa-bookmark"></i>
          申请流程
        </p>
        <ul class="stepUl stepUl3">
          <li>
            <span>
              <i class="fa fa-file"></i> 用户提交存储资料使用申请
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-address-book-o"></i> 管理员审核分配存储资源
            </span>
          </li>
          <li>
            <span>
              <i class="fa fa-retweet"></i> 返回存储资源使用账户密码等信息
            </span>
          </li>
        </ul>
      </div>
      <el-card class="box-card">
        <el-row>
          <el-col :span="10" class="imgBox">
            <img src="@/assets/home/images/banner4.gif" alt />
          </el-col>
          <el-col :span="14" class="textBox">
            <p
              style="padding-left:20px;"
            >用户从气象大数据云平台中提交存储资料使用申请。 管理员数据存储管理系统中对用户提交的申请进行审核，并利用相关业务存储资源管理工具进行资源分配和账号设置。 管理员将分配好的存储资源使用账号、密码等信息返回给用户。</p>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </div>
</template>
<script>
import "font-awesome/css/font-awesome.css";
export default {
  data() {
    return {
      pageFlag: this.$route.params.pageFlag
    };
  },
  mounted() {
    if (this.pageFlag) {
      document.querySelector("#" + this.pageFlag).scrollIntoView(true);
    }
  },
  methods: {}
};
</script>
<style lang="scss">
.businessVue {
  min-width: 1200px;
  margin: auto;
  .sectionTop {
    cursor: pointer;
    width: 60%;
    margin: auto;
  }
  h5.title {
    font-size: 28px;
    font-weight: 300;
    margin-top: 64px;
    margin-bottom: 50px;
    text-align: center;
  }
  .topImg {
    display: block;
    width: 25%;
    margin: auto;
    -webkit-transition: 0.4s;
    -webkit-transition: -webkit-transform 0.4s ease-out;
    transition: transform 0.4s ease-out;
    -moz-transition: -moz-transform 0.4s ease-out;
  }
  .inline-item:hover img {
    transform: rotateZ(360deg);
    -webkit-transform: rotateZ(360deg);
    -moz-transform: rotateZ(360deg);
  }
  .imgtips {
    color: #333;
    font-size: 14px;
    text-align: center;
  }
  .stepBox {
    height: 170px;
    background: #3ab0f0;
    position: relative;
    background: url("~@/views/systemHelp/bussiness/img/footBk.png");
    background-size: 100% 100%;
    margin-bottom: 12px;
  }
  .stepTitle {
    width: 28px;
    padding: 8px;
    background: #fff;
    color: #3ab0f0;
    left: 10px;
    font-size: 14px;
    position: absolute;
  }
  .stepUl {
    padding: 0;
    margin: 0 auto;
    width: 100%;
    counter-reset: step;
    padding-top: 41px;
  }
  .stepUl li {
    list-style-type: none;
    font-size: 14px;
    float: left;
    position: relative;
    width: 16.5%;
    text-align: center;
    color: #fff;
  }
  .stepUl2 li {
    width: 25%;
  }
  .stepUl3 li {
    width: 33.3%;
  }
  .stepUl li:before {
    content: counter(step);
    counter-increment: step;
    width: 20px;
    line-height: 18px;
    display: block;
    font-size: 12px;
    color: #3ab0f0;
    background: #fff;
    border-radius: 50%;
    margin: 0 auto 18px auto;
    text-align: center;
    border: 1px solid #fff;
    box-shadow: 1px 1px 1px #999;
  }
  .stepUl li:after {
    content: "";
    width: 80%;
    height: 1px;
    background: #fff;
    position: absolute;
    left: -40%;
    top: 14px;
  }
  .stepUl li:first-child:after {
    content: none;
  }
  .stepUl li span {
    display: inline-block;
    background: #fff;
    color: #3ab0f0;
    padding: 10px;
    border-radius: 4px;
    position: relative;
    box-shadow: 1px 1px 1px #999;
  }
  .stepUl li span:after {
    content: "";
    position: absolute;
    top: -14px;
    border-left: 7px solid transparent;
    border-top: 7px solid transparent;
    border-right: 7px solid transparent;
    border-bottom: 7px solid #fff;
    left: 44%;
  }
  .imgBox {
    img {
      width: 100%;
    }
  }
  .textBox {
    p {
      margin-top: 45px;
      text-indent: 28px;
    }
  }
}
</style>
