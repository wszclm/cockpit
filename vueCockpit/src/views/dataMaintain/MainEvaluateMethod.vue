<template>
    <div class="synthesize-main">
        <div class="synthesize-header">
            <a-button type="primary" class="synthesize-header-button" @click="handleAdd">
                新增评价
            </a-button>
        </div>

        <div class="synthesize-content">


            <div class="synthesize-content-list" v-for="(item,key) in list" :key="key">
                <div class="synthesize-content-list-header">
                    {{key==1?"评价办法列表-已使用":"评价办法列表-未使用"}}
                </div>
                <div class="synthesize-content-list-main">
                    <div class="synthesize-content-list-main-item" v-for="(e,i) in item" :key="i">
                        <a-card hoverable class="synthesize-content-list-main-item-card"  tabChange="xxx">
                            <img
                                    slot="cover"
                                    alt="example"
                                    src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png"
                            />
                            <template slot="actions" class="ant-card-actions">
                                <div @click="handleEdit(e)">编辑</div>
                                <div v-if="key==2" @click="handleUnder(e)">上架</div>
                                <div v-if="key==1" @click="handleUnder(e)">下架</div>
                                <div @click="handleCopy(e)">复制</div>
                                <div @click="handleDel(e)">删除</div>
                            </template>
                            <a-card-meta :title="e.name" description="">
                            </a-card-meta>
                        </a-card>
                    </div>
                </div>
            </div>


        </div>


    </div>
</template>

<script>

    const List = {
        1:[
            {
                id:3,
                name:"桐庐开发区2020年亩均开发面积",
                status:1,
            },
            {
                id:4,
                name:"桐庐开发区2020年亩均开发面积",
                status:1,
            },
            {
                id:5,
                name:"桐庐开发区2020年亩均开发面积",
                status:1,
            },
            {
                id:6,
                name:"桐庐开发区2020年亩均开发面积",
                status:1,
            }
        ],
        2:[
            {
                id:1,
                name:"桐庐开发区2020年亩均开发面积",
                status:0,
            },
            {
                id:2,
                name:"桐庐开发区2020年亩均开发面积",
                status:0,
            }
        ],

    }
    export default {
        name: "MainEvaluateMethod" ,// 综合评价办法
        data(){
            return {
                list:List

            }
        },
        created() {
            this.getEvalMethod();
        },
        methods:{
            xxx(x){
                console.log(x)
            },
            handleEdit(e){
                this.$router.push({path: '/indicators',query:{id: e.id,title:e.name,onUse:e.status}})
                console.log(e)
            },
            handleUnder(e){
                console.log(e)
            },
            handleCopy(e){
                console.log(e)
            },
            handleDel(e){
                this.$confirm('确定要删除选中的一条记录吗？注意本操作不可恢复', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let id= e.id;
                    if(id==null){
                        this.$alert("请至少选择一条记录！");
                        return;
                    }
                    this.deleteRequest(this.$CST.MAIN_EVAL_METHOD_DEL,{id:id}).then(resp => {
                        if (resp) {
                            if(resp.meta.statusCd == -1 || resp.meta.statusCd == 400){
                                this.$alert("删除失败："+resp.meta.message,'Error');
                                return;
                            }
                            if (resp.meta.statusCd == 200) {
                                this.getEvalMethod();
                                this.$alert("删除成功"+resp.meta.message,'Success');
                            }
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            //获取亩均评价办法
            getEvalMethod() {
                let params = {
                };
                this.getRequest(this.$CST.MAIN_EVAL_METHOD_LIST, params).then(resp => {
                    this.loading = false;
                    if (resp) {
                        console.log(resp.info);
                        this.list = resp.info.data;
                    }else{
                        this.$alert('亩均评价办法查询失败：' + reason,'error')
                    }
                }).catch((reason) => {
                    this.$alert('亩均评价办法查询失败：' + reason,'error')
                }).finally(() => {
                    this.loaded = true;
                    this.loading = false;
                })
            },

            //新增评价跳转
            handleAdd(){
                this.$router.push('/indicators')

            }
        },
    }
</script>

<style scoped>
    .synthesize-header{
        padding: 20px 0;
    }
    .synthesize-main{
        margin: 20px;
    }
    .synthesize-header-button{
        margin-right: 20px;
    }

    .synthesize-content-list{
        border-top: 1px solid #c3c3c3;
    }
    .synthesize-content-list-header{
        font-size: 16px;
        color: #ff0000;
        line-height: 50px;
    }
    .synthesize-content-list-main{
        display: flex;
        flex-wrap:wrap;
    }
    .synthesize-content-list-main-item{
        padding: 0 20px 20px 0;
        width: 300px;
    }
    /*.synthesize-content-list-main-item-card{*/
        /*background-color: #1890ff;*/
    /*}*/
</style>