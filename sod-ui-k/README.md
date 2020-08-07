## ����

```bash
# ��¡��Ŀ
git clone https://gitee.com/y_project/RuoYi-Vue

# ������ĿĿ¼
cd ruoyi-ui

# ��װ����
npm install

# ���鲻Ҫֱ��ʹ�� cnpm ��װ���������и��ֹ���� bug������ͨ�����²������ npm �����ٶ���������
npm install --registry=https://registry.npm.taobao.org

# ��������
npm run dev
```

��������� http://localhost:80

## ����

```bash
# �������Ի���
npm run build:stage

# ������������
npm run build:prod
```
<!-- 
科大保留的功能
【存储结构管理】里面的【存储结构概览】、【存储字段检索】、【表结构管理】、【表结构导出】
structureManagement:[overviewStorage,filedSearch,tableStructureManage,exportTable]
【系统管理】里面的【数据库字典管理】（除了服务代码管理）、【字典管理】、【SQL模板管理】
dbDictMangement:[dataUsageMangement,dbManagement,manageField]
【用户管理】内的全部
system
【系统帮助】里的【数据库文档】
systemHelp:[dataBaseDevFile]
 -->