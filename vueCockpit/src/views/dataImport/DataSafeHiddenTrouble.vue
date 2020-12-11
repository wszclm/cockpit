<template>
 <div>
        <div class='marketing' >
            <div class='marketing-content'>
                <el-row :gutter='10'>
                    <el-col :span='24' align='right'>
                        <el-button type='success' icon='el-icon-download'> <a style="color:#fff" href="../static/file/安全隐患.xls" download="安全隐患.xls">安全隐患模板下载</a></el-button>
                        <el-button type='primary' icon='el-icon-delete-solid' @click='batchRemove'>删除</el-button>
                          </el-col>
                          <el-col>
                           <el-upload
                            ref="upload"
                            action='/apis/hiddenDanger/batchImport' 
                            :with-credentials='true' 
                            :on-progress='onUploadProgress'
                            :on-success='mtdUploadSuccess'
                            list-type="file">
                            <el-button size="small" type="primary" v-loading="fileImporting">点击上传</el-button>
                            </el-upload>
                    </el-col>
                </el-row>
                <el-row :gutter='10' style='margin-top: 1%;'>
                    <el-col :span='24' >
                        <el-table ref="table" :data='tableData' :border=true :stripe=true v-loading='loading' @selection-change="changeFun" element-loading-text='拼命加载中'>
                            <el-table-column type="selection" prop='id' align='center' width='70' label='序号'></el-table-column>
                            <el-table-column prop='ename' align='center' label='企业名称' ></el-table-column>
                            <el-table-column prop='content' align='center' label='隐患内容'></el-table-column>
                            <el-table-column prop='createDate' align='center' label='创建时间'></el-table-column>
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
    </div>
</template>

<script>
  import {mymessage} from '@/utils/mymessage';
import { Message } from 'element-ui';
export default {
    name: 'DataSafeHiddenTrouble',
    data() {
        return {
            loaded: false,
            loadErr: false,
            total: 0,
            cur_page: 1,
            pageSize:10,
            fileImporting:false,
            name:'',
            tableData: [],
            selectRow:[],
            loading: false,
            popVisible: false,
            popVisible2: false,
            title: '',
            dialogVisible: false,
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
          //文件上传后从response返回id,name写入attach_files
        mtdUploadSuccess:function(res, file, fileList){
            try{
                if(!!res.meta.statusCd && res.meta.statusCd != '200'){
                    this.$alert(res.meta.message,'上传失败')
                }else{
                    this.$alert(res.meta.message,'上传成功')
                    this.$refs.upload.clearFiles();
                    this.getTableData();
                };
            }catch(err){
                this.$alert(res.message,'上传失败')
            }finally{
                this.$refs.upload.clearFiles();
                this.fileImporting = false;
            }
        },
        onUploadProgress: function () {
            this.fileImporting = true;
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
                this.deleteRequest('/hiddenDanger/deleteById',{ids:ids}).then(resp => {
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
                pageNo: this.cur_page,
                pageSize: this.pageSize,
            };
            this.getRequest('/hiddenDanger/queryAll').then(resp => {
                this.loading = false;
                if (resp) {
                    console.log(resp.info);
                    this.total = resp.info.total;
                    this.tableData = resp.info.data;
                }else{
                    this.alert('数据查询失败：' + reason,'error')
                }
            }).catch((reason) => {
                this.alert('数据查询失败：' + reason,'error')
            }).finally(() => {
                this.loaded = true;
                this.loading = false;
            })
        },
        changeFun(row){
            this.selectRow = row;
        },
       
        trim(s) {
            return s.replace(/(^\s*)|(\s*$)/g, '');
        }
    }
}
</script>

<style scoped>

</style>