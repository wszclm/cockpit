<template>
    <el-dialog title="租赁数据导入" top='1%' :visible.sync="isShow"  size="large" class='min-body queryOffer edit-dialog' lock-scroll :modal='true'>
                    <el-form :model="form">
                        <el-row class="margin-bottom-10">
                          
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
                                    action='/apis/leaseMng/batchImport' 
                                    :with-credentials='true' 
                                    :on-progress='onUploadProgress'
                                    :on-success='mtdUploadSuccess'
                                    :on-error='mtdUploadError'
                                    list-type="file">
                                    <el-button size="small" type="primary" v-loading="fileImporting">点击上传</el-button>
                                </el-upload>
                            </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="下载模板：" :label-width="formLabelWidth">
                                <span><a href="../static/file/租赁数据导入模板.xlsx" download="企业租赁数据导入模板.xlsx">企业租赁数据导入模板（Excel）</a>请严格遵循模板格式，否则会导入失败</span>
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
.separator-line {
    margin-top: 5px;
    border-top: 1px solid #e4eaec;
}
</style>
<script>
import {Message} from 'element-ui';
export default {
    name: 'EnterpriseLeaseLoad',
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
            console.log('上传文件失败')
            console.log(response);
            console.log(err);
            this.$alert('上传文件失败','上传异常');
            this.fileImporting = false;
        },
        onUploadProgress: function () {
            this.fileImporting = true;
        },
        beforeUpload(file){
            console.log(file,'文件');
            this.files = file;
            const extension = file.name.split('.')[1] === 'xls'
            const extension2 = file.name.split('.')[1] === 'xlsx'
            const isLt2M = file.size / 1024 / 1024 < 5
            if (!extension && !extension2) {
                Message.warning('上传模板只能是 xls、xlsx格式!')
                return
            }
            if (!isLt2M) {
                Message.warning('上传模板大小不能超过 5MB!')
                return
            }
            this.fileName = file.name;
            return false // 返回false不会自动上传
        },
        submitUpload() {
            console.log('上传'+this.fileName);
            if(this.fileName==""){
                Message.warning('请选择要上传的文件！')
                return false
            }
            this.postMultRequest(this.$CST.LOAD_ENTERPRISE_LEASE_IMPORT).then(resp => {
                try{
                    if(!!resp.meta.statusCd && resp.meta.statusCd != '200'){
                        this.$message({
                            message: '操作成功',
                            type: 'success',
                            duration: 1500,
                        })
                    }else{
                        Message.error(resp.meta.message);
                    };
                }catch(err){
                    Message.error(resp.meta.message);
                }finally{
                    this.isShow = false;
                }

            })
            this.isShow = false;
        },
        calAction: function() {
            // 附件上传
        var ret = this.$CST.API_ENTERPRISE_LEASE_FILE;
        return ret;
        },
    },
    computed: {
     
    },
    watch: {}
}

</script>
