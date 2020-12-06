<template>
    <div class="indicators-main">
        <div class="indicators-header">
            <div class="indicators-header-item">
                <a-button type="primary" class="indicators-header-button" @click="handleAdd">
                    新增指标
                </a-button>
                <a-button type="primary" class="indicators-header-button" @click="handleSave">
                    保存
                </a-button>

                <a-button class="indicators-header-button" @click="handleReturn">
                    返回
                </a-button>

            </div>
            <div class="indicators-header-item">
                <a-button type="primary">
                    指标导出
                </a-button>
            </div>

        </div>

        <div class="indicators-content">
            <div class="indocators-content-header">
                <div>评价名称：</div>
                <a-input class="indocators-content-input" placeholder="请输入标题" v-model="title"/>
            </div>
            <div class="indocators-content-title">
                <a-tabs type="card" v-model="tabValue">
                    <a-tab-pane key="1" tab="指标列表">
                        <list-template :data="list1" tabIndex="1" @change="changeList" @edit="changeEditList"></list-template>
                    </a-tab-pane>
                    <a-tab-pane key="2" tab="定档">
                        <list-template :data="list2" tabIndex="2" @change="changeList" @edit="changeEditList"></list-template>
                    </a-tab-pane>
                    <a-tab-pane key="3" tab="提档">
                        <list-template :data="list3" tabIndex="3" @change="changeList" @edit="changeEditList"></list-template>
                    </a-tab-pane>
                    <a-tab-pane key="4" tab="降档">
                        <list-template :data="list4" tabIndex="4" @change="changeList" @edit="changeEditList"></list-template>
                    </a-tab-pane>
                </a-tabs>
            </div>
        </div>
    </div>
</template>

<script>

    import ListTemplate from './ListTemplate'

    const NewData1 = [
        [
            {
                index:'zb',
                title:'指标名称',
                value:'亩均税收',
                input:1,
            },
            {
                index:'qz',
                title:'权重',
                value:'50',
                input:1,
            },
            {
                index:'pj',
                title:'评价属性',
                value:'',
                select:1,
                selectValue:'1',
            },
            {
                index:'jz',
                title:'基准值',
                value:'19',
                input:1,
            },
            {
                index:'sx',
                title:'上限',
                value:'100',
                input:1,
            },
            {
                index:'sjly',
                title:'数据来源',
                value:'',
                select:1,
                selectValue:'1',
            },
            {
                index:'dw',
                title:'单位',
                value:'万元',
                input:1,
            },
            {
                index:'xx',
                title:'下限',
                value:'0',
                input:1,
            },
            {

            }
        ],
        [
            {
                index:'zb',
                title:'指标名称',
                value:'亩均税收',
                input:1,
            },
            {
                index:'qz',
                title:'权重',
                value:'50',
                input:1,
            },
            {
                index:'pj',
                title:'评价属性',
                value:'',
                select:1,
                selectValue:'1',
            },
            {
                index:'jz',
                title:'基准值',
                value:'19',
                input:1,
            },
            {
                index:'sx',
                title:'上限',
                value:'100',
                input:1,
            },
            {
                index:'sjly',
                title:'数据来源',
                value:'',
                select:1,
                selectValue:'1',
            },
            {
                index:'dw',
                title:'单位',
                value:'万元',
                input:1,
            },
            {
                index:'xx',
                title:'下限',
                value:'0',
                input:1,
            },
            {

            }
        ],

    ]

    const NewData2 = [
        [
            {
                index:'ddyy',
                title:'定档原因',
                value:'县500强企业',
                input:1,
            },
            {
                index:'dddw',
                title:'调整档位',
                value:'',
                select:1,
                selectValue:'1',
            },
        ]

    ]
    const NewData3 = [
        [
            {
                index:'tdyy',
                title:'提档原因',
                value:'成长型科技企业',
                input:1,
            },
            {
                index:'tddw',
                title:'调整档位',
                value:'',
                select:1,
                selectValue:'2',
            },
        ]

    ]
    const NewData4 = [
        [
            {
                index:'jdyy',
                title:'降档原因',
                value:'规上企业下降为规下',
                input:1,
            },
            {
                index:'jddw',
                title:'调整档位',
                value:'',
                select:1,
                selectValue:'3',
            },
        ]

    ]


    export default {
        name: "MainIndicators",

        data(){
            return {
                tabValue:'1',
                evalId:'',
                onUse:'',
                title:"新增亩均评价办法",
                list1:NewData1,
                list2:NewData2,
                list3:NewData3,
                list4:NewData4,
            }
        },

        components:{
            ListTemplate
        },
        created(){
            const id = this.$route.query.id;
            const name=this.$route.query.title;
            const onUse=this.$route.query.onUse;
            if(typeof id !== "undefined" && id !== null){
                this.id=id;
                this.getEvalMethodDeatil(id);
            }
            if(typeof name !== "undefined" && name !== null){
                this.title=name;
            }
            if(typeof onUse !== "undefined" && onUse !== null){
                this.onUse=onUse;
            }
        },
        mounted(){

        },

        methods:{
            //获取亩均评价明细
            getEvalMethodDeatil(id) {
                let params = {
                    id:id
                };
                this.getRequest(this.$CST.MAIN_EVAL_METHOD_DETAIL, params).then(resp => {
                    this.loading = false;
                    if (resp) {
                        console.log(resp.info);
                        this.list1 = resp.info.data.NewData1;
                        this.list2 = resp.info.data.NewData2;
                        this.list3 = resp.info.data.NewData3;
                        this.list4 = resp.info.data.NewData4;
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
            handleAdd(){
                const arr = "list"+this.tabValue;

                //var listArr = [];
                let list=JSON.parse(JSON.stringify(this[arr][0]));

                list.forEach((e)=>{
                    e.value=''
                    e.selectValue = 0
                })
                this[arr].push(list)
            },

            handleSave(){
                console.log(this.title)
                console.log(this.list1)
                console.log(this.list2)
                console.log(this.list3)
                console.log(this.list4)
                let params = {
                    title: this.title,
                    evalId:this.id,
                    onUse:this.onUse,
                    list1: this.list1,
                    list2: this.list2,
                    list3: this.list3,
                    list4: this.list4,
                };
                this.postRequest(this.$CST.MAIN_EVAL_METHOD_ADD, params).then(resp => {
                    this.loading = false;
                    if (resp) {
                        if(resp.meta.statusCd == -1 || resp.meta.statusCd == 400){
                            this.$alert("保存失败："+resp.meta.message,'Error');
                            return;
                        }
                        if (resp.meta.statusCd == 200) {
                            this.$alert("保存成功"+resp.meta.message,'Success');
                        }
                    }
                }).catch((reason) => {
                    this.$alert('亩均评价办法保存失败：' + reason,'error')
                }).finally(() => {
                    this.loaded = true;
                    this.loading = false;
                })
            },

            handleReturn(){
                this.$router.push('/mainEvaluateMethod')
            },


            changeList(e){

                const arr = "list"+e.tabIndex;
                console.log(this[arr])

                let res = this[arr].filter((num,k) => {
                    console.log(num)
                    if(k==e.index){
                        return false
                    }else{
                        return true
                    }
                });
                this[arr] = res
            },

            changeEditList(e){
                debugger
                console.log(e)
                const arr = "list"+e.tabIndex;
                console.log(this[arr])
                if(e.type==1){
                    this[arr][e.i][e.j].selectValue = e.value
                }else{
                    this[arr][e.i][e.j].value = e.value
                }

            }

        }
    }
</script>

<style scoped>
    .indicators-main{
        margin: 20px;
    }
    .indicators-header{
        padding: 20px 0;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        border-bottom: 1px solid #c3c3c3;
    }
    .indicators-header-button{
        margin-right: 20px;
    }

    .indocators-content-header{
        display: flex;
        height: 32px;
        line-height: 32px;
        margin: 20px 0;
    }

    .indocators-content-input{
        width: 20%;
    }
    .indocators-content-title{
        margin: 20px 0;
    }

</style>