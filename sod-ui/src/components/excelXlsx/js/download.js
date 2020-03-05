//导出的方法
export function exportExcel(tableHeader, tableProp, tableData) {
  import('@/components/excelXlsx/js/Export2Excel').then(excel => {
    //注意这个Export2Excel路径
    /* const tHeader = ['序号', '昵称', '姓名'];   // 上面设置Excel的表格第一行的标题
    const filterVal = ['index', 'nickName', 'name']; // 上面的index、nickName、name是tableData里对象的属性key值
    const list = this.tableData;  //把要导出的数据tableData存到list
    const data = this.formatJson(filterVal, list);
    export_json_to_excel(tHeader, data, '列表excel');//最后一个是表名字 */
    const tHeader = tableHeader; // 上面设置Excel的表格第一行的标题
    const filterVal = tableProp; // 上面的index、nickName、name是tableData里对象的属性key值
    const list = tableData; //把要导出的数据tableData存到list
    const data = formatJson(filterVal, list);
    excel.export_json_to_excel(tHeader, data, '列表excel'); //最后一个是表名字
  })
}
export function formatJson(filterVal, jsonData) {
  return jsonData.map(v => filterVal.map(j => v[j]))
}
