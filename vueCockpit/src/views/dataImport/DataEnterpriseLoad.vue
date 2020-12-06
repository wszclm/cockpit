<template>
    <el-dialog title="企业数据导入" top='1%' :visible.sync="isShow"  size="large" class='min-body queryOffer edit-dialog' lock-scroll :modal='true'>
                    <el-form :model="form">
                        <el-row class="margin-bottom-10">
                            <el-col :span="24">
                            <el-form-item label="数据年份：" :label-width="formLabelWidth">
                             <el-select v-model="year" placeholder="请选择年份">   
                             <el-option v-for="(item) in optionyear" 
                                   :key="item.text" :label="item.text" 
                                    :value="item.text">
                             </el-option>
                             </el-select>
                                <el-select v-model="month" placeholder="请选择月份">
                                     <el-option v-for="(item) in optionmonth" 
                                   :key="item.text" :label="item.text" 
                                    :value="item.text">
                             </el-option>
                                </el-select>
                            </el-form-item>
                            </el-col>
                           <!-- <el-col :span="24">
                             <el-form-item label="导入文件：" :label-width="formLabelWidth">
                            <el-upload
                                    class="upload-demo"
                                    ref="upload"
                                    action="doUpload"
                                    :limit="1"
                                    :multiple="false"
                                    :file-list="fileList"
                                    :before-upload="beforeUpload">
                                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                                <a href="../../../public/static/file/国土数据导入模板.xlsx" rel="external nofollow" download="模板"><el-button size="small" type="success">下载模板</el-button></a>
                                <div slot="tip" class="el-upload__tip">只能上传excel文件，且不超过5MB</div>
                                <div slot="tip" class="el-upload-list__item-name">{{fileName}}</div>
                            </el-upload>
                             </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="" :label-width="formLabelWidth">
                                    <el-button @click="isShow = false">取消</el-button>
                                    <el-button type="primary" @click="submitUpload()">确定</el-button>
                                </el-form-item>
                            </el-col>-->
                            <el-col :span="24">
                            <el-form-item label="导入文件：" :label-width="formLabelWidth">
                                <el-upload
                                    ref="upload"
                                    action='no' 
                                    :on-progress='onUploadProgress'
                                    :http-request="uploadOk"
                                    :on-success='mtdUploadSuccess' 
                                    :on-error='mtdUploadError'
                                    list-type="file">
                                     <!-- <el-button slot="trigger" size="small" type="primary">选取文件</el-button> -->
                                    <el-button size="small" type="primary" v-loading="fileImporting" @click="submitUpload">点击上传</el-button>
                                </el-upload>
                            </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="下载模板：" :label-width="formLabelWidth">
                                <span><a href="../static/file/企业数据导入模板.xlsx" download="企业数据导入模板.xlsx">企业数据导入模板（Excel）</a>请严格遵循模板格式，否则会导入失败</span>
                            </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                  <el-form-item label="" :label-width="formLabelWidth">
                                  <el-button @click="isShow = false">取 消</el-button>
                                  <el-button type="primary" @click="isShow = false">确 定</el-button>
                                  </el-form-item>
                             </el-col>
                        </el-row>
                    </el-form>
        <div class="separator-line"></div>
    </el-dialog>
</template>
<style scoped lang='less'>
.search-input-50 {
    display: inline-block;
    width: 300px;
}

</style>
<script>
export default {
    name: 'DataEnterpriseLoad',
    beforeMount: function() {},
    mounted: function() {
        console.log(this.name)
    },
    data: function() {
        return {
            // name: 'landDataLoad',
            loading: false,
            isShow: false,
            fileName:'',
            data: [],
            year:'2020',
            month:'11',
            optionyear:[
              {text:"2019"},
              {text:"2020"},
              {text:"2021"},
              {text:"2022"},
              {text:"2023"},
              {text:"2024"},
              {text:"2025"},
              {text:"2026"},
              {text:"2027"},
              {text:"2028"},
              {text:"2029"},
              {text:"2030"},
               
            ],
            optionmonth:[
                {text:"01"},
                {text:"02"},
                {text:"03"},
                {text:"04"},
                {text:"05"},
                {text:"06"},
                {text:"07"},
                {text:"08"},
                {text:"09"},
                {text:"10"},
                {text:"11"},
                {text:"12"},

            ],
            fileImporting:false,
            form: {
                name: '',
                region: '',
                date1: '',
                date2: '',
                delivery: false,
                type: [],
                resource: '',
                desc: ''
            },
            formLabelWidth: '120px',
            props: {
                value: 'labelGroupId',
                label: 'labelGroupName',
                children: 'children',
            },
            conditions: {
            },
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

        }
    },
    methods: {
        
         submitUpload() {
                this.$refs.upload.submit();
            },    

            uploadOk(val){
                if(!this.year || !this.year){
                    this.$alert("请选择导入年份和月份",'Success');  
                   return;
                }
                let fd = new FormData();
                fd.append('year',this.year);
                fd.append('month',this.month);
                fd.append('file',val.file);
                this.postMultRequest(this.$CST.LOAD_ENTERPRISE_IMPORT, fd).then(resp => {
                this.loading = false;
                if (resp) {
                    if(resp.meta.statusCd == '200'){
                       this.$alert("添加成功："+resp.meta.message,'Success');  
                       this.getTableData();
                    }
                }else{
                   this.$alert("添加失败："+resp.meta.message,'Error');  
                }
            }).catch((reason) => {
                this.$alert("添加失败："+resp.meta.message,'Error');  
            }).finally(() => {
                this.loaded = true;
                this.loading = false;
                })
            },

        //文件上传后从response返回id,name写入attach_files
        mtdUploadSuccess:function(res, file, fileList){
            try{
                if(!!res.meta.statusCd && res.meta.statusCd != '200'){
                    this.$alert(res.meta.message,'上传失败')
                }else{
                    this.$alert(res.meta.message,'上传成功')
                    this.getTableData();
                };
            }catch(err){
                console.log(err)
                this.$alert(res.message,'上传失败')
            }finally{
                this.fileImporting = false;
            }
        },

        mtdUploadError:function(err, response, file){
             this.$alert(response.meta.message,'上传失败')
            this.fileImporting = false;
        },

        onUploadProgress: function () {
            this.fileImporting = true;
        }

    //     //文件上传后从response返回id,name写入attach_files
    //     mtdUploadSuccess:function(res, file, fileList){
    //         try{
    //             if(!!res.meta.statusCd && res.meta.statusCd != '200'){
    //                 this.$alert(res.meta.message,'上传失败')
    //             }else{
    //                 this.$alert(res.meta.message,'上传成功')
    //                 this.getTableData();
    //             };
    //         }catch(err){
    //             console.log(err)
    //             this.$alert(res.message,'上传失败')
    //         }finally{
    //             this.fileImporting = false;
    //         }
    //     },
    //     mtdUploadError:function(err, response, file){
    //         console.log('上传文件失败')
    //         console.log(response);
    //         console.log(err);
    //         this.$alert('上传文件失败','上传异常');
    //         this.fileImporting = false;
    //     },
    //     onUploadProgress: function () {
    //         this.fileImporting = true;
    //     },
    //     beforeUpload(file){
    //         console.log(file,'文件');
    //         this.files = file;
    //         const extension = file.name.split('.')[1] === 'xls'
    //         const extension2 = file.name.split('.')[1] === 'xlsx'
    //         const isLt2M = file.size / 1024 / 1024 < 5
    //         if (!extension && !extension2) {
    //             this.$message.warning('上传模板只能是 xls、xlsx格式!')
    //             return
    //         }
    //         if (!isLt2M) {
    //             this.$message.warning('上传模板大小不能超过 5MB!')
    //             return
    //         }
    //         this.fileName = file.name;
    //         return false // 返回false不会自动上传
    //     },
    //     submitUpload() {
    //         console.log('上传'+this.files.name)
    //         if(this.fileName == ""){
    //             this.$message.warning('请选择要上传的文件！')
    //             return false
    //         }
    //         let params = {
    //             year: this.year,
    //             month: this.month
    //         };
    //         this.postMultRequest(this.$CST.LOAD_ENTERPRISE_IMPORT, params).then(resp => {
    //             try{
    //                 if(!!resp.meta.statusCd && resp.meta.statusCd != '200'){
    //                     this.$message({
    //                         message: '操作成功',
    //                         type: 'success',
    //                         duration: 1500,
    //                     })
    //                 }else{
    //                     this.$message.error(resp.meta.message);
    //                 };
    //             }catch(err){
    //                 this.$message.error(resp.meta.message);
    //             }finally{
    //                 this.isShow = false;
    //             }

    //         })
    //     }
    },
    computed: {
        calAction: function() {
             // 附件上传
            var ret = this.$CST.API_ENTERPRISE_UPLOAD_FILE.replace('{year}', this.year).replace('{month}', this.month);
            return ret;
        },
    },
    watch: {}
}

</script>
