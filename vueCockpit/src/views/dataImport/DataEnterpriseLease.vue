<template>
    <div>
        <div class='marketing' >
            <div class='marketing-content'>
                <el-row :gutter='10'>
                    <el-col :span='4'>
                        <el-input v-model='enterpriseName' placeholder="请输入主体企业名称进行搜索.." />
                    </el-col>
                    <el-col :span='4'>
                        <el-button icon="el-icon-search" type="primary" @click='getTableData'>搜索</el-button>
                    </el-col>
                    <el-col :span='16' align='right'>
                        <el-button  type='primary' icon='el-icon-circle-plus-outline' @click='addEnterpriseLease' >新增</el-button>
                        <el-button type='primary' icon='el-icon-delete-solid' @click='batchRemove'>删除</el-button>
                        <el-button type='success' icon='el-icon-upload2' @click='handleAddLabel' >数据导入</el-button>
                        <el-button type='success' icon='el-icon-download' @click='batchImport'>数据导出</el-button>
                    </el-col>
                </el-row>
                <el-row :gutter='10' style='margin-top: 1%;'>
                    <el-col :span='24' >
                        <el-table ref="table" :data='tableData' :border=true :stripe=true v-loading='loading' @selection-change="changeFun" element-loading-text='拼命加载中'>
                            <el-table-column type="selection" align='center' width='70' label='序号'></el-table-column>
                            <el-table-column prop='parcelNum' align='center' label='宗地号' ></el-table-column>
                            <el-table-column prop='enterPriseName' align='center' label='主体企业'></el-table-column>
                            <el-table-column prop='enterPriseCode' align='center' label='主体企业社会信用代码'></el-table-column>
                            <el-table-column prop='leaseEnterPriseName' align='center' label='租赁企业'></el-table-column>
                            <el-table-column prop='leaseEnterPriseCode' align='center' label='租赁企业信用代码'></el-table-column>
                            <el-table-column prop='leaseArea' align='center' label='租赁面积'></el-table-column>
                            <el-table-column prop='leaseStartDate' align='center' label='租赁日期'></el-table-column>
                            <el-table-column prop='remark' align='center' label='租期'></el-table-column>
                              <el-table-column prop='createDate' align='center' label='数据导入时间'></el-table-column>
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

             <el-dialog
                :title="title"
                :visible.sync="dialogVisible"
                width="70%">
            <div>
                <el-form :model="emp" :rules="rules" ref="empForm">
                    <el-row>
                        <el-col :span="7" align='middle'>
                            <el-form-item label="宗地号:" prop="parcelNum">
                                <el-input size="mini" style="width: 180px" prefix-icon="el-icon-edit" v-model="emp.parcelNum"
                                          placeholder="请输入宗地号"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="主体企业:" prop="enterpriseName">
                                <el-input size="mini" style="width: 180px" prefix-icon="el-icon-edit" v-model="emp.enterpriseName"
                                          placeholder="请输入主体企业名称"></el-input>
                            </el-form-item>
                        </el-col>
                         <el-col :span="8">
                            <el-form-item label="主体企业社会信用代码:" prop="enterpriseCode">
                                <el-input size="mini" style="width: 180px" prefix-icon="el-icon-edit" v-model="emp.enterpriseCode"
                                          placeholder="请输入主体企业统一认证编码"></el-input>
                            </el-form-item>
                        </el-col>
                         </el-row>
                        <el-row>
                        <el-col :span="7">
                            <el-form-item label="租赁企业:" prop="leaseEnterpriseName">
                                <el-input size="mini" style="width: 180px" prefix-icon="el-icon-edit" v-model="emp.leaseEnterpriseName"
                                          placeholder="请输入租赁企业名称"></el-input>
                            </el-form-item>
                        </el-col>
                         <el-col :span="9">
                            <el-form-item label="租赁企业社会信用代码:" prop="leaseEnterpriseCode">
                                <el-input size="mini" style="width: 180px" prefix-icon="el-icon-edit" v-model="emp.leaseEnterpriseCode"
                                          placeholder="请输入租赁企业社会信用代码"></el-input>
                            </el-form-item>
                        </el-col>

                         <el-col :span="8">
                            <el-form-item label="租赁面积:" prop="leaseArea">
                                <el-input size="mini" style="width: 180px" prefix-icon="el-icon-edit"
                                          v-model="emp.leaseEnterpriseArea" placeholder="请输入租赁面积（单位：平方）"></el-input>
                            </el-form-item>
                        </el-col>
                        </el-row>
                         <el-row>
                        <el-col :span="7">
                            <el-form-item label="租赁日期:" prop="startDate">
                                <el-date-picker
                                        v-model="emp.leaseStartDate"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 170px;"
                                        placeholder="租赁开始时间">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>

                          <el-col :span="7">
                            <el-form-item label="租赁到期:" prop="endDate">
                                <el-date-picker
                                        v-model="emp.leaseEndDate"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 170px;"
                                        placeholder="租赁到期时间">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="doAddEmp">确 定</el-button>
        </span>
        </el-dialog>
        </div>
        <EnterpriseLeaseLoad ref="enterpriseLeaseLoad" :getTableData="getTableData"></EnterpriseLeaseLoad>
    </div>
</template>
<style scoped lang='less'>
.title-font{
    font-size: 18px;
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
import EnterpriseLeaseLoad from './EnterpriseLeaseLoad';
import {mymessage} from '@/utils/mymessage';
import { Message } from 'element-ui';
export default {
    name: 'DataEnterpriseLease',
    data() {
        return {
            enterpriseName:'',
            loading: false,
            loaded: false,
            loadErr: false,
            total: 0,
            cur_page: 1,
            pageSize:10,
            name:'',
            tableData: [],
            selectRow:[],
            allDeps: [],
            politicsstatus: [],
            nations: [],
            loading: false,
            popVisible: false,
            popVisible2: false,
            positions: [],
            joblevels: [],
            defaultProps: {
            },
            title: '',
            dialogVisible: false,
            emp: {
                parcelNum: "",
                enterpriseName: "",
                enterpriseCode: "",
                leaseEnterpriseName: "",
                leaseEnterpriseCode: "",
                leaseEnterpriseArea: "",
                leaseStartDate: "",
                leaseEndDate: ""
            },
               rules: {
                    parcelNum: [{required: true, message: '请输入宗地号', trigger: 'blur'}],
                    enterpriseName: [{required: true, message: '请输入主体企业名称', trigger: 'blur'}],
                    enterpriseCode: [{required: true, message: '请输入主体企业社会信用代码', trigger: 'blur'}],
                    leaseEnterpriseName: [{required: true, message: '请输入租赁企业名称', trigger: 'blur'}],
                    leaseEnterpriseCode: [{required: true, message: '请输入租赁企业社会信用代码', trigger: 'blur'}],
                    leaseEnterpriseArea: [{required: true, message: '请输入租赁面积', trigger: 'blur'}],
                    leaseStartDate: [{required: true, message: '请输入租赁开始时间', trigger: 'blur'}],
                    leaseEndDate: [{required: true, message: '请输入租赁到期时间', trigger: 'blur'}],
                }
        }
    },
    created() {
        this.getTableData();
    },
    components: {
        EnterpriseLeaseLoad
    },
    methods: {
        emptyEmp() {
            this.emp = {
                parcelNum: "",
                enterpriseName: "",
                enterpriseCode: "",
                leaseEnterpriseName: "",
                leaseEnterpriseCode: "",
                leaseEnterpriseArea: "",
                leaseStartDate: "",
                leaseEndDate: "",
            }
        },
        doAddEmp() {
            if (this.emp.id) {
                this.$refs['empForm'].validate(valid => {
                    if (valid) {
                        this.putRequest("/leaseMng/add", this.emp).then(resp => {
                              if(resp.meta.statusCd == -1){
                                this.alert('企业租赁数据添加失败：' + resp.meta.info,'error')
                            }
                            if (resp) {
                                this.dialogVisible = false;
                                this.initEmps();
                            }
                        })
                    }
                });
            } else {
                this.$refs['empForm'].validate(valid => {
                    if (valid) {
                        this.putRequest(this.$CST.ENTERPRISE_LEASE_ADD, this.emp).then(resp => {
                            if(resp.meta.statusCd == -1){
                               // mymessage.error({message: resp.meta.message? resp.meta.message : '添加失败!'});          
                                    this.$alert("添加失败："+resp.meta.message,'Error');  
                                    return;                         
                                }
                            if (resp.meta.statusCd == 200) {
                                this.$alert("添加成功："+resp.meta.message,'Success');  
                                this.dialogVisible = false;
                                this.getTableData();
                            }
                        })
                    }
                });
            }
        },

        batchRemove(){
            this.$confirm('此操作将永久删除选中的企业租赁数据, 是否继续?', '提示', {
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

                this.deleteRequest(this.$CST.ENTERPRISE_LEASE_DEL,{ids:ids}).then(resp => {
                     if(resp.meta.statusCd == -1 || resp.meta.statusCd == 400){
                        // mymessage.error({message: resp.meta.message? resp.meta.message : '添加失败!'});          
                            this.$alert("删除失败："+resp.meta.message,'Error');  
                            return;                         
                        }
                    if (resp.meta.statusCd == 200) {
                        this.getTableData();
                         this.$alert("批量删除成功"+resp.meta.message,'Success'); 
                    }
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },

        handleAddLabel() {
            this.$refs.enterpriseLeaseLoad.isShow = true;
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
                pageNo: this.cur_page,
                pageSize: this.pageSize,
            };
            this.getRequest(this.$CST.LOAD_ENTERPRISE_LEASE_LOAD, params).then(resp => {
                this.loading = false;
                if (resp) {
                    console.log(resp.info);
                    this.total = resp.info.total;
                    this.tableData = resp.info.data;
                }else{
                    this.alert('企业租赁数据查询失败：' + reason,'error')
                }
            }).catch((reason) => {
                this.alert('企业租赁数据查询失败：' + reason,'error')
            }).finally(() => {
                this.loaded = true;
                this.loading = false;
            })
        },
        changeFun(row){
            this.selectRow = row;
        },
       
        addEnterpriseLease(){
            this.emptyEmp();
            this.title = '添加企业租赁关系';
            this.dialogVisible = true;
        },

        batchImport(){
           //先经过查询再导出
              let params = {
                enterpriseName: this.trim(this.enterpriseName),
                pageNo: 1,
                pageSize: 10000,
            };
            if(this.tableData.length > 0){
               this.getRequest(this.$CST.LOAD_ENTERPRISE_LEASE_LOAD, params).then(resp => {
                let arr = resp.info.data
                if(arr.length == params.pageSize){
                  this.$alert('目前最大可导出10000条数据！', '提示', {
                    confirmButtonText: '确定',
                  });
                  return
                }
                this.$utils.exportExcel(arr,this.$refs.table.columns,'企业租赁数据导出')
              }).catch((reason) => {
                this.$alert('企业租赁数据导出查询失败：' + reason, '提示', {
                  confirmButtonText: '确定',
                });
              }).finally(()=>{
                this.loaded = true;
                this.loading = false
              })
            }else{
              this.$alert('请先查询数据再导出！', '提示', {
                confirmButtonText: '确定',
               });
              }
        },

        trim(s) {
            return s.replace(/(^\s*)|(\s*$)/g, '');
        }
    }
}
</script>
