<template>
    <el-dialog
        title="新增企业数据"
        :visible.sync="show"
        :before-close="handleClose"
        style="width: 1200px;margin-left:auto; margin-right:auto;" center>
        <el-form  :model="enterprise"  :rules="rules" label-width="140px" label-suffix=':' ref='dataForm'>
        <el-row style="margin-top: -10px">
            <el-col :span="12">
                <el-form-item label='企业名称' prop='enterpriseName'>
                <el-input placeholder=""
                          v-model="enterprise.enterpriseName"
                          style="width:150px;display:inline-block">
                </el-input>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label='状态' prop='status'>
                <el-input placeholder=""
                          v-model="enterprise.status"
                          style="width:150px;display:inline-block">
                </el-input>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label='统一社会信用代码' prop='enterpriseCode'>
                <el-input placeholder=""
                          v-model="enterprise.enterpriseCode"
                          style="width:150px;display:inline-block">
                </el-input>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label='企业类别' prop='enterpriseType'>
                <el-input placeholder=""
                          v-model="enterprise.enterpriseType"
                          style="width:150px;display:inline-block">
                </el-input>
          </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label='企业规模' prop='enterpriseScale'>
                <el-input placeholder=""
                          v-model="enterprise.enterpriseScale"
                          style="width:150px;display:inline-block">
                </el-input>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label='所属网络' prop='belongNetWork'>
            <el-input placeholder=""
                      v-model="enterprise.belongNetWork"
                      style="width:150px;display:inline-block">
            </el-input>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label='行业类别' prop='industryType'>
                <el-input placeholder=""
                          v-model="enterprise.industryType"
                          style="width:150px;display:inline-block">
                </el-input>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label='详细地址' prop='detailAddress'>
                <el-input placeholder=""
                          v-model="enterprise.detailAddress"
                          style="width:150px;display:inline-block">
                </el-input>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label='联系人' prop='contact'>
                <el-input placeholder=""
                          v-model="enterprise.contact"
                          style="width:150px;display:inline-block">
                </el-input>
            </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label='联系电话' prop='contactNum'>
            <el-input placeholder=""
                      v-model="enterprise.contactNum"
                      style="width:150px;display:inline-block">
            </el-input>
                </el-form-item>
            </el-col>
        </el-row>
         <el-row class="button_group" style="text-align:center">
             <div style="display:inline-block; *display:inline; zoom:1;">
                <el-button type="primary" @click="handleClose" class="button_cancel">取消</el-button>
                 <el-button type="primary" @click="saveData" class="button_q">确定</el-button>
             </div>
         </el-row>
        </el-form>
    </el-dialog>
</template>

<script>

    export default {
        name: "DataEnterpriseAdd",
        data() {
            return {
                enterprise:{
                    enterpriseName:'',
                    status:'',
                    enterpriseCode: '',
                    enterpriseType: '',
                    enterpriseScale: '',
                    belongNetWork: '',
                    industryType:'',
                    detailAddress:'',
                    contact:'',
                    contactNum:''
                },
                flag: false,
                show: false,
                rules: {
                    enterpriseName: [{required: true, message: '请输入企业名称', trigger: 'blur'}],
                    status: [{required: true, message: '请输入状态', trigger: 'blur'}],
                    enterpriseCode: [{required: true, message: '请输入统一社会信用代码', trigger: 'blur'}],
                    enterpriseType: [{required: true, message: '请输入企业类别', trigger: 'blur'}],
                    enterpriseScale: [{required: true, message: '请输入企业规模', trigger: 'blur'}],
                    belongNetWork: [{required: true, message: '请输入所属网络', trigger: 'blur'}],
                    industryType: [{required: true, message: '请输入行业类别', trigger: 'blur'}],
                    detailAddress: [{required: true, message: '请输入详细地址', trigger: 'blur'}],
                    contact: [{required: true, message: '请输入联系人', trigger: 'blur'}],
                    contactNum: [{required: true, message: '请输入联系电话', trigger: 'blur'}]
                }
            }
        },

        props: {
            onSelect: {
                type: Function,
                default: function() {

                }
            },
            getTableData: {

                type: Function,

                default: function() {

                }

            },
            year:{

                type: Number

            },
            month:{

                type: Number

            },
        },

        mounted: function () {
        },

        methods: {

            handleClose() {
                this.show = false;
            },
            emptyEmp() {
                this.enterprise = {
                    enterpriseName:'',
                    status:'',
                    enterpriseCode: '',
                    enterpriseType: '',
                    enterpriseScale: '',
                    belongNetWork: '',
                    industryType:'',
                    detailAddress:'',
                    contact:'',
                    contactNum:'',
                }
            },
            //编辑配置
/*            editConfiguration() {
                this.state='';
                let params = {};
                if (!!this.value) {
                    var statrDate = dateFunc.format(this.value, "YYYY-MM-DD");
                    Object.assign(params, {'effDate': statrDate});
                }
                if (!!this.value1) {
                    var endDate = dateFunc.format(this.value1, "YYYY-MM-DD");
                    Object.assign(params, {'expDate': endDate});
                }
                Object.assign(params, {
                    deal: 'update',
                    commonRegionId: this.regionId.toString(),
                    valueType: this.valueType,
                    offerType: this.offerType,
                    offerThresholdId: this.offerThresholdId,
                    value: this.threshold,
                    staffId: this.staffId,
                })
                this.$fetch(this.$CST.API_THRESHOLD_CONFIGURATION_UPDATE, 'post', params).then((res) => {
                    this.state=res.data.isSuccess;
                }).catch((reason) => {
                    alert('编辑阈值失败：' + reason);
                }).finally(() => {
                    let offerTypeName='';
                    if(this.offerType==='11'){
                        offerTypeName='套餐'
                    }else{
                        offerTypeName='可选包+促销'
                    }
                    if("N"===this.state){
                        alert("该省"+offerTypeName+"的提醒阈值大于（等于）告警阈值，无法修改");
                    }else{
                        alert("修改成功");
                        this.handleClose();
                        EventBus.$emit("getData");
                    }
                })
            },*/

            //新增配置
            addConfiguration() {
                /*let params = {};
                Object.assign(params, {
                    enterpriseName:this.enterpriseName,
                    status:this.status,
                    enterpriseCode: this.enterpriseCode,
                    enterpriseType: this.enterpriseType,
                    enterpriseScale: this.enterpriseScale,
                    belongNetWork: this.belongNetWork,
                    industryType:this.industryType,
                    detailAddress:this.detailAddress,
                    contact:this.contact,
                    contactNum:this.contactNum,
                })
                this.postRequest(this.$CST.LOAD_ENTERPRISE_ADD, this.enterprise).then(resp => {
                    if(!!resp.meta.statusCd && resp.meta.statusCd != '200'){
                        this.$alert(resp.meta.message,'新增企业信息失败')
                    }else{
                        this.$alert(resp.meta.message,'新增企业信息成功')
                        this.getTableData();
                    };
                }).catch((reason) => {
                    alert('新增企业信息失败：' + reason);
                }).finally(() => {
                    this.handleClose();
                })*/
                Object.assign(this.enterprise, {'year': this.year});
                Object.assign(this.enterprise, {'month': this.month});
                this.$refs['dataForm'].validate(valid => {
                    if (valid) {
                        this.postRequest(this.$CST.LOAD_ENTERPRISE_ADD, this.enterprise).then(resp => {
                            if(resp.meta.statusCd == -1){
                                this.$alert("新增企业信息失败："+resp.meta.message,'Error');
                                return;
                            }
                            if (resp.meta.statusCd == 200) {
                                this.$alert("新增企业信息成功："+resp.meta.message,'Success');
                                this.dialogVisible = false;
                                this.getTableData();
                            }
                        }).catch((reason) => {
                            this.$alert("新增企业信息失败："+reason);
                        }).finally(() => {
                            this.handleClose();
                        })
                    }
                });
                this.emptyEmp();
            },

            //点击保存按钮
            saveData() {
                this.addConfiguration()
             /*   if ('add' === this.deal) {
                    this.addConfiguration()
                } else if ('edit' === this.deal) {
                    this.editConfiguration()
                }*/
            },

/*            checkValue() {
                if(this.regionId&&this.offerType&&this.value&&this.value1&&this.threshold&&this.valueType){
                    let start=new Date(this.value);
                    let end =new Date(dateFunc.format(this.value1, "YYYY-MM-DD"));
                    let startTime=start.getTime();
                    let endTime=end.getTime();
                    if(start < end|| startTime=== endTime){
                        this.saveData();
                    }else{
                        alert("失效时间应大于等于生效时间,请检查");
                    }
                }else{
                    alert("请将配置信息填写完整");
                }
            },*/
        }
    }
</script>

<style scoped>
    .condition {
        margin-top: 10px;
        margin-left: 50px;
    }

    .condition_title{
        margin-top: 10px;
    }

    .button_group {
        margin-top: 10px;
    }

    .button_q {
        color: #303133;
        background: #20A0FF;
        border: 1px #909399 solid;
        font-size: 16px;
        border-radius: 50px;
        padding: 5px 8px;
        float: left;
        margin-left: 30px
    }

    .button_cancel {
        color: #909399;
        background: #fff;
        border: 1px #909399 solid;
        font-size: 16px;
        border-radius: 50px;
        padding: 5px 8px;
        float: left;
    }

</style>
