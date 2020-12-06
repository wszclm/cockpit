<template>
    <div>
        <div class='marketing' >
            <div class='marketing-content'>
                <el-row :gutter='10'>
                    <el-col :span='4'>
                        <el-select v-model='year' placeholder='年度'>
                            <el-option
                                v-for='item in years'
                                :key='item.value'
                                :label='item.label'
                                :value='item.value'>
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span='4'>
                        <el-select v-model='month' placeholder='月份'>
                            <el-option
                                    v-for='item in months'
                                    :key='item.value'
                                    :label='item.label'
                                    :value='item.value'>
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span='4'>
                        <el-input v-model='enterpriseName' placeholder='按照企业名称搜索' />
                    </el-col>
                    <el-col :span='4'>
                        <el-button icon="el-icon-search" type="primary" @click='getTableData'>搜索</el-button>
                    </el-col>
                    <el-col :span='8' align='right'>
                        <el-button type='success' icon='el-icon-upload2' @click='handleAddLabel' >数据导入</el-button>
                        <el-button type='primary' icon='el-icon-delete-solid' @click='batchRemove'>删除</el-button>
                    </el-col>
                </el-row>
                <el-row :gutter='10' style='margin-top: 1%;'>
                    <el-col :span='24' >
                        <el-table :data='tableData' :border=true :stripe=true v-loading='loading' @selection-change="changeFun" element-loading-text='拼命加载中'>
                            <el-table-column type="selection" align='center' width='70' label='序号'></el-table-column>
                            <el-table-column prop='enterPriseName' align='center' label='企业名称' ></el-table-column>
                            <el-table-column prop='enterPriseCode' align='center' label='统一社会信用代码'></el-table-column>
                            <el-table-column prop='registerArea' align='center' label='登记用地面积（亩）'></el-table-column>
                            <el-table-column prop='lesseeArea' align='center' label='承租用地面积（亩）'></el-table-column>
                            <el-table-column prop='leaseArea' align='center' label='出租用地面积（亩）'></el-table-column>
                            <el-table-column prop='createDate' align='center' label='导入时间'></el-table-column>
                        </el-table>
                    </el-col>
                </el-row>
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
        <DataLandLoad ref="dataLandLoad" :getTableData="getTableData"></DataLandLoad>
    </div>
</template>
<style scoped lang='less'>
.title-font{
    font-size: 16px;
    font-weight: 600;
    padding:1em;
}
.marketing {
    margin-right: 10px;
    .top {
        margin-bottom: 20px;
    }
    .marketing-content {
        background-color: #fff;
        padding: 15px;
        .button_submit {
            float: right;
        }
    }
}
</style>
<style lang='less'>
.marketing {
    .marketing-title {
        font-size: 18px;
        line-height: 36px;
        height: 36px;
    }
}
.el-button--info.is-plain {
    background: #fff;
    border-color: #ccd5db;
    color: #62a8ea;
}
</style>
<script>
import DataLandLoad from './DataLandLoad'

export default {
    name: 'DataLand',
    data() {
        return {
        years: [
{
	value: '2019',
	label: '2019年度'
}, {
	value: '2020',
	label: '2020年度'
},
{
	value: '2021',
	label: '2021年度'
}, {
	value: '2022',
	label: '2022年度'
},
{
	value: '2023',
	label: '2023年度'
}, {
	value: '2024',
	label: '2024年度'
},
{
	value: '2025',
	label: '2025年度'
}, {
	value: '2026',
	label: '2026年度'
},
{
	value: '2027',
	label: '2027年度'
}, {
	value: '2028',
	label: '2028年度'
}, {
	value: '2029',
	label: '2029年度'
},{
	value: '2030',
	label: '2030年度'
}
],
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
            year:'2020',
            month:'11',
            enterpriseName:'',
            loading: false,
            loaded: false,
            loadErr: false,
            total: 0,
            cur_page: 1,
            pageSize: this.$CST.PAGE_SIZE,
            name:'',
            tableData: [],
            selectRow:[],
            defaultProps: {
            },
        }
    },
    created() {
        this.getTableData();
    },
    components: {
        DataLandLoad
    },
    methods: {
        handleAddLabel() {
            this.$refs.dataLandLoad.isShow = true;
        },
        handleCurrentChange(val) {
            this.cur_page = val;
            this.getTableData();
        },
        handleSizeChange(val) {
            this.cur_page = 1;
            this.pageSize = val;
            this.getTableData();
        },
        getTableData() {
            let params = {
                enterpriseName: this.trim(this.enterpriseName),
                year: this.year,
                month: this.month,
                pageNo: this.cur_page,
                pageSize: this.pageSize,
            };
            this.getRequest(this.$CST.LOAD_LAND_DATA, params).then(resp => {
                this.loading = false;
                if (resp) {
                    console.log(resp.info);
                    this.total = resp.info.total;
                    this.tableData = resp.info.data;
                }else{
                    this.alert('国土数据查询失败：' + reason,'error')
                }
            }).catch((reason) => {
                this.alert('国土数据查询失败：' + reason,'error')
            }).finally(() => {
                this.loaded = true;
                this.loading = false;
            })
        },
        changeFun(row){
            this.selectRow = row;
        },
        batchRemove(){
                this.$confirm('此操作将永久删除选中的企业国土数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let ids = [];
                    for (let i = 0; i < this.selectRow.length; i++) {
                        ids.push(this.selectRow[i].id)
                    }
                if(ids.length ==0){
                     this.$alert("请至少选择一条记录！"); 
                     return; 
                }
                this.deleteRequest(this.$CST.LOAD_LAND_DEL,{ids:ids}).then(resp => {
                      if (resp) {
                        if(resp.meta.statusCd == -1 || resp.meta.statusCd == 400){
                    // mymessage.error({message: resp.meta.message? resp.meta.message : '添加失败!'});          
                        this.$alert("删除失败："+resp.meta.message,'Error');  
                        return;                         
                    }
                    if (resp.meta.statusCd == 200) {
                        this.getTableData();
                        this.$alert("批量删除成功"+resp.meta.message,'Success'); 
                      }
                   }
                })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });




            /*if(this.selectRow.length != 0){
                this.$confirm('将删除选中的企业国土数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                    lockScroll: false
                }).then(()=> {
                    let ids = [];
                    for (let i = 0; i < this.selectRow.length; i++) {
                        ids.push(this.selectRow[i].id)
                    }
                    this.deleteRequest(this.$CST.LOAD_LAND_DEL, {ids:ids}).then(res => {
                        this.total = res.info.total;
                        this.tableData = res.info.data;
                        alert('批量删除成功' );
                    }).catch((reason) => {
                        alert('批量删除失败：' + reason);
                    }).finally(() => {})
                }).catch(()=> {});
            }else{
                this.$alert("请先选择要批量删除的企业国土数据", '提示', {
                    confirmButtonText: '确定',
                });
            }*/
        },
        trim(s) {
            return s.replace(/(^\s*)|(\s*$)/g, '');
        }
    }
}
</script>
