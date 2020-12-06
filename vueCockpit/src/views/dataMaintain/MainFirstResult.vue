<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div>
          <el-select v-model='year' placeholder='年度' @change="initCountData" clearable  style="width: 105px;margin-right: 10px">
            <el-option
                v-for='item in years'
                :key='item.value'
                :label='item.label'
                :value='item.value'>
            </el-option>
          </el-select>
          <el-select v-model='month' placeholder='月份' @change="initCountData" clearable  style="width: 80px;margin-right: 10px">
            <el-option
                v-for='item in months'
                :key='item.value'
                :label='item.label'
                :value='item.value'>
            </el-option>
          </el-select>
          <el-input placeholder="请输入企业名称或者宗地号" prefix-icon="el-icon-search"
                    clearable
                    @clear="mainDataList"
                    style="width: 300px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="mainDataList" ></el-input>
          <el-button icon="el-icon-search" type="primary" @click="mainDataList">
            搜索
          </el-button>
        </div>
        <div>
          <el-button type="danger" icon="fa fa-pied-piper-alt" style="font-size:12px; font-weight:bold;">
            工业用地：{{gongyeArea}} 家
          </el-button>
          <el-button type="danger"  icon="fa fa-check-square-o"  style="font-size:12px; font-weight:bold;">
            规上企业：{{guishangEnterprise}} 家
          </el-button>
          <el-button type="danger"  icon="fa fa-rmb"  style="font-size:12px; font-weight:bold;">
            高薪企业：{{gaoxinEnterprise}} 家
          </el-button>
        </div>
        <div>
          <el-button type="success" @click="exportData" icon="el-icon-download">
            导出数据
          </el-button>
        </div>
      </div>
        <div style="border: 0px solid #409eff;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0px;">
          <el-row :gutter='10' style="margin-top: 10px">
            <el-col :span='4'>
              <el-select v-model='searchValue.scale' placeholder='***显示所有规模***' @change="mainDataList('advanced')" clearable>
                <el-option
                    v-for='item in scales'
                    :key='item.value'
                    :label='item.label'
                    :value='item.value'>
                </el-option>
              </el-select>
            </el-col>
            <el-col :span='4'>
              <el-select v-model='searchValue.netWork' placeholder='***显示所有网格***' @change="mainDataList('advanced')" clearable>
                <el-option
                    v-for='item in netWorks'
                    :key='item.value'
                    :label='item.label'
                    :value='item.value'>
                </el-option>
              </el-select>
            </el-col>
            <el-col :span='4'>
              <el-select v-model='searchValue.state' placeholder='***显示所有状态***' @change="mainDataList('advanced')" clearable >
                <el-option
                    v-for='item in states'
                    :key='item.value'
                    :label='item.label'
                    :value='item.value'>
                </el-option>
              </el-select>
            </el-col>
            <el-col :span='4'>
              <el-select v-model='searchValue.type' placeholder='***显示所有企业类别***' @change="mainDataList('advanced')" clearable>
                <el-option
                    v-for='item in types'
                    :key='item.value'
                    :label='item.label'
                    :value='item.value'>
                </el-option>
              </el-select>
            </el-col>
            <el-col :span='4'>
              <el-select v-model='searchValue.type' placeholder='***显示所有等级***' @change="mainDataList('advanced')" clearable>
                <el-option
                    v-for='item in grades'
                    :key='item.value'
                    :label='item.label'
                    :value='item.value'>
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          ref="table"
          :header-cell-style="{background:'#eef1f6',color:'#606266'}"
          :data="tableData"
          :stripe=true
          :border=true
          v-loading="loading"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          style="width: 100%">
        <el-table-column
            prop="rank"
            align="left"
            label="名次"
            width="120">
        </el-table-column>
        <el-table-column
            prop="parcelNum"
            align="left"
            label="宗地号"
            width="120">
        </el-table-column>
        <el-table-column
            prop="enterPriseName"
            label="主体企业"
            align="left"
            width="100">
        </el-table-column>
        <el-table-column prop='enterPriseCode'  align="left"  width="120"  label='统一社会信用代码'></el-table-column>
        <el-table-column prop='industryCode'  align="left"  width="80"  label='行业代码'></el-table-column>
        <el-table-column prop='evalGrade'  align="left"  width="80"  label='评价等级'></el-table-column>
        <el-table-column prop='totalScore'  align="left"  width="80"  label='总分数'></el-table-column>
        <el-table-column
            prop="area"
            label="用地面积（亩）"
            align="left"
            width="120">
        </el-table-column>
        <el-table-column
            prop="registerArea"
            label="登记用地面积（亩）"
            align="left"
            width="120">
        </el-table-column>
        <el-table-column
            prop="rentArea"
            label="承租用地面积（亩）"
            align="left"
            width="120">
        </el-table-column>
        <el-table-column
            prop="hireArea"
            label="出租用地面积（亩）"
            align="left"
            width="120">
        </el-table-column>
         <el-table-column
              prop="buildArea"
              width="100"
              label="建筑面积（平方米）">
      </el-table-column>
         <el-table-column
                prop="plotRatio"
                width="80"
                label="容积率">
        </el-table-column>
        <el-table-column
            prop="scale"
            width="100"
            label="规模">
        </el-table-column>
        <el-table-column
            prop="netWork"
            width="80"
            align="left"
            label="网格">
        </el-table-column>
        <el-table-column
            prop="state"
            width="80"
            align="left"
            label="状态">
        </el-table-column>
        <el-table-column
            prop="type"
            width="80"
            align="left"
            label="企业类别">
        </el-table-column>
        <el-table-column
            prop="taxation"
            width="100"
            align="left"
            label="实缴税收（万元）">
        </el-table-column>
        <el-table-column
            prop="industryPro"
            width="100"
            align="left"
            label="主营收入（万元）">
        </el-table-column>
        <el-table-column
            prop="RD"
            width="120"
            align="left"
            label="R&D经营占主营业务收入比重">
        </el-table-column>
        <el-table-column label="亩均税收" align="center">
          <el-table-column
              prop="moneyUnit"
              width="90"
              align="left"
              label="万元/亩">
          </el-table-column>
          <el-table-column
              prop="mjScore"
              width="90"
              label="分数">
          </el-table-column>
        </el-table-column>
        <el-table-column label="R&D经营占主营业务收入比重" align="center">
          <el-table-column
              prop="rdUnit"
              width="100"
              align="left"
              label="%">
          </el-table-column>
          <el-table-column
              prop="rdScore"
              width="120"
              label="分数">
          </el-table-column>
        </el-table-column>
      </el-table>
      <div style="display: flex;justify-content: flex-end">
        <div class='pagination'>
          <el-pagination background  layout='total, sizes,prev, pager, next, jumper'
                         :page-size='pageSize'
                         :page-sizes='[7, 10, 20, 50]'
                         :total='total'
                         @current-change='handleCurrentChange'
                         @size-change='handleSizeChange'>
          </el-pagination>
        </div>
      </div>
    </div>
  </div>
</template>
<style scoped lang='less'>
.firstTable{
  width: 100%;
  border-bottom: 1px solid #e6e6e6;
  padding: 20px 0 50px 0;
}
.secondTable{
  width: 100%;
  padding: 30px 0 0 0;
}
.spanShow{
  font: outline;
  font-size: 17px;
  color: #e6a23c;
  font-weight: bold;
  padding: 0 0 10px 0;
}
.dataShow{
  font-size: 14px;
  color: #c7000c;
}
.zulin{
  padding: 0 0 10px 0;
}
</style>
<script>
import {mymessage} from '@/utils/mymessage';
export default {
  name: "MainFirstResult", //初次评价结果
  data() {
    return {
      /** 统计数据start**/
      gongyeArea:121,
      guishangEnterprise:200,
      gaoxinEnterprise:500,
      /** 统计数据start**/
      years: [{
        value: '2019',
        label: '2019年度'
      }, {
        value: '2020',
        label: '2020年度'
      }],
      months: [
        {
          value: '1',
          label: '1月份'
        },
        {
          value: '2',
          label: '2月份'
        },
        {
          value: '3',
          label: '3月份'
        },
        {
          value: '4',
          label: '4月份'
        },
        {
          value: '5',
          label: '5月份'
        },
        {
          value: '6',
          label: '6月份'
        },
        {
          value: '7',
          label: '7月份'
        },
        {
          value: '8',
          label: '8月份'
        },
        {
          value: '9',
          label: '9月份'
        },
        {
          value: '10',
          label: '10月份'
        },
        {
          value: '11',
          label: '11月份'
        },
        {
          value: '12',
          label: '12月份'
        }
      ],
      year:'',
      month:'',
      searchValue: {
        scale:null,
        netWork:null,
        state:null,
        type: null,
      },
      scales: [{
        value: '规上企业',
        label: '规上企业'
      }, {
        value: '规下企业',
        label: '规下企业'
      }],
      grades: [{
        value: '1',
        label: 'A类'
      }, {
        value: '2',
        label: 'B类'
      },{
        value: '3',
        label: 'C类'
      }, {
        value: '4',
        label: 'D类'
      }],
      netWorks: [{
        value: '第一网格',
        label: '第一网格'
      }, {
        value: '第二网格',
        label: '第二网格'
      },{
        value: '第三网格',
        label: '第三网格'
      }, {
        value: '第四网格',
        label: '第四网格'
      }],
      states: [{
        value: '参与评价（正常企业）',
        label: '参与评价（正常企业）'
      }, {
        value: '参与评价（无评价等级）',
        label: '参与评价（无评价等级）'
      },{
        value: '不参与评价（新建投产企业）',
        label: '不参与评价（新建投产企业）'
      }, {
        value: '参与评价（其他）',
        label: '参与评价（其他）'
      }],
      types: [{
        value: '年度新增企业',
        label: '年度新增企业'
      }, {
        value: '年度退出企业',
        label: '年度退出企业'
      },{
        value: '高新技术企业',
        label: '高新技术企业'
      }, {
        value: '雏鹰计划企业',
        label: '雏鹰计划企业'
      },{
        value: '小升规企业',
        label: '小升规企业'
      }, {
        value: '瞪羚企业',
        label: '瞪羚企业'
      }, {
        value: '降规企业',
        label: '降规企业'
      },{
        value: '蒲公英计划企业',
        label: '蒲公英计划企业'
      }, {
        value: '不参与评价（其他）',
        label: '不参与评价（其他）'
      }],
      title: '土地详情信息',
      title2:'主体企业详情信息',
      tableData:[],
      tableColumnData:[],
      loading: false,
      columnloading:false,
      popVisible: false,
      popVisible2: false,
      dialogVisible: false,
      qiyeDialogVisible:false,
      typeVal:"",
      total: 0,
      cur_page: 1,
      pageSize:10,
      keyword: '',
      size: 10,
      rules: {

      }
    }
  },
  created() {
    this.mainDataList();
  },

  mounted() {
  },
  methods: {
    handleCurrentChange(val) {
      this.cur_page = val;
    },
    handleSizeChange(val) {
      this.cur_page = 1;
      this.pageSize = val;
      this.mainDataList();
    },
    mainDataList(type) {
      if(!this.year){
        mymessage.warning("请选择年份");
        this.tableData=[];
        return;
      }
      if(!this.month){
        mymessage.warning("请选择月份");
        this.tableData=[];
        return;
      }
      this.loading = true;
      let params
      let url = this.$CST.MAIN_DATA_LIST;
      if (type && type == 'advanced') {
        this.typeVal = 'advanced';
        params = this.getParams(type);
      }else{
        params = this.getParams("keyword");
        this.typeVal = 'keyword';
      }
      this.getRequest(url, params).then(resp => {
        this.loading = false;
        if (resp) {
          this.total = resp.info.total;
          this.tableData = resp.info.data;
        }else{
          this.loading = false;
          this.alert('综合数据查询失败：' + reason,'error')
        }
      }).catch((reason) => {
        this.loading = false;
        this.alert('综合数据列表查询失败：' + reason,'error')
      }).finally(() => {
        this.loading = false;
      })
      this.loading = false;
    },

    initCountData(){
      this.mainDataList();
      let url = this.$CST.MAIN_DATA_COUNT;
      let params = {
        year:this.year,
        month:this.month
      }
      this.getRequest(url, params).then(resp => {
        if (resp) {
          this.guishangEnterprise = resp.info.enterpriseScale;
        }
      }).catch((reason) => {
        console.log('综合数据列表查询失败：' + reason,'error');
      });
    },
    exportData() {
      //数据导出
      if(!this.year){
        mymessage.warning("请选择年份");
        this.tableData=[];
        return;
      }
      if(!this.month){
        mymessage.warning("请选择月份");
        this.tableData=[];
        return;
      }
      let exportparams
      let url = this.$CST.MAIN_DATA_LIST;
      if (this.typeVal && this.typeVal == 'advanced') {
        exportparams = this.getParams(this.typeVal);
      }else{
        exportparams = this.getParams("keyword");
      }
      exportparams.pageNo= 1;
      exportparams.pageSize= 10000;
      if(this.tableData.length>0){
        this.getRequest(url, exportparams).then(resp => {
          if (resp) {
            let arr = resp.info.data
            if(arr.length > exportparams.pageSize){
              this.$alert('目前最大可导出10000条数据！', '提示', {
                confirmButtonText: '确定',
              });
              return
            }
            this.$utils.exportExcel(arr,this.$refs.table.columns,'企业综合数据导出')
          }else{
            this.loading = false;
            this.alert('综合数据导出失败：' + reason,'error')
          }
        }).catch((reason) => {
          this.loading = false;
          this.$alert('企业租赁数据导出查询失败：' + reason, '提示', {
            confirmButtonText: '确定',
          });
        })
      }else{
        this.$alert('请先查询数据再导出！', '提示', {
          confirmButtonText: '确定',
        });
      }
    },
    getParams(type){
      let params;
      params = {
        pageNo: this.cur_page,
        pageSize: this.pageSize,
        year:this.year,
        month:this.month
      }
      if(type && type == 'advanced'){
        params = {
          pageNo: this.cur_page,
          pageSize: this.pageSize,
          year:this.year,
          month:this.month,
          enterpriseScale: this.searchValue.scale,
          belongNetWork:this.searchValue.netWork,
          status:this.searchValue.state,
          enterpriseType:this.searchValue.type,
        }
      }
      if(type && type == 'keyword'){
        params = {
          pageNo: this.cur_page,
          pageSize: this.pageSize,
          year:this.year,
          month:this.month,
          enterpriseName:this.keyword
        }
      }
      return params;
    },
    trim(s) {
      return s.replace(/(^\s*)|(\s*$)/g, '');
    },
  }
}
</script>
