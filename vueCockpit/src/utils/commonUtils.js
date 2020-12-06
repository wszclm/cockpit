
/**
 * 公共方法
 **/

export default class commonUtils {

  /**
   * 去空格
   * @param str
   * @returns {boolean}
   */
  static trim(s) {
    return s.replace(/(^\s*)|(\s*$)/g, "");
  }

  /**
   * 判断字符串是否为空
   * @param str
   * @returns {boolean}
   */
  static isNull(str) {
    return str == null || str.length === 0 || str === '';
  }


  /**
	 * 导出excel
	 * @param {*} arr 需要到处的数据
	 * @param {*} columns 列
	 * @param {*} name excel名称
	 */
	static exportExcel(arr,columns,name){
      if(Array.isArray(arr) && arr.length != 0){
        let excelHeader = []
        let dataKey = []
        columns.forEach(obj => {
          if(obj.property){
            excelHeader.push(obj.label)
            dataKey.push(obj.property)
          }
        });
        require.ensure([], () => {
          let {exportJsonToExcel} = require('@/vendor/Export2Excel');
          exportJsonToExcel(excelHeader,arr.map(v => dataKey.map(j => v[j])),name);
        })
      }else{
        alert('请先查询数据再导出', '提示', {
          confirmButtonText: '确定',
        });
      }
    }

}

