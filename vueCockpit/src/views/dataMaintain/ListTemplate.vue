<template>
    <div class="xx-common-main">
        <div class="xx-common-item" v-for="(item,i) in data" :key="i">
            <a-icon type="close-circle" @click="handleDel(i)" class="xx-common-item-close" style="font-size: 30px"/>
            <div :class="item.length==2?'xx-common-item-i xx-common-item-i-50':'xx-common-item-i'" v-for="(e,key) in item" :key="key">

                <div class="xx-common-item-c xx-common-item-table">
                    {{e.title}}
                </div>
                <div class="xx-common-item-c" v-if="e.input==1">
                    <input class="xx-common-item-input" :value="e.value" :data-i="i" :data-j="key" @change="handleInput"/>
                </div>
                <div class="xx-common-item-c xx-common-item-c-center" v-if="e.select==1">
                    <div v-if="tabIndex==1">
                        <a-select :value="e.selectValue==undefined?0:e.selectValue" class="xx-common-item-select" @change="handleChange">
                            <a-select-option :value="m.index" :i="i" :j="key" v-for="(m,index) in select1" :key="index">
                                {{m.name}}
                            </a-select-option>
                        </a-select>
                    </div>
                    <div v-else>
                        <a-select :value="e.selectValue==undefined?0:e.selectValue" class="xx-common-item-select" @change="handleChange">
                            <a-select-option :value="m.index" :i="i" :j="key" v-for="(m,index) in select2" :key="index">
                                {{m.name}}
                            </a-select-option>
                        </a-select>
                    </div>
                </div>
            </div>
        </div>


    </div>
</template>

<script>
    const select1 = [
        {
            index:'0',
            name:'***评价所有企业***'
        },
        {
            index:'1',
            name:'评价规上企业'
        },
        {
            index:'2',
            name:'评价规下企业'
        },

    ];
    const select2 = [
        {
            index:'0',
            name:'***选择档位***'
        },
        {
            index:'1',
            name:'锁定A档'
        },
        {
            index:'2',
            name:'锁定B档'
        },
        {
            index:'3',
            name:'锁定C档'
        },
        {
            index:'4',
            name:'锁定D档'
        },
        {
            index:'5',
            name:'降低1档'
        },
        {
            index:'6',
            name:'降低2档'
        },
        {
            index:'7',
            name:'降为C或D档'
        },


    ];
    export default {
        name: "list-template",
        data(){
            return {
                select1,
                select2,

            }
        },

        props:{
            data:Array,
            tabIndex:String
        },

        methods:{
            handleChange(e,option){
                this.$emit('edit',{tabIndex:this.tabIndex,i:option.data.attrs.i,j:option.data.attrs.j,value:e,type:1})
            },

            handleDel(e){
                var that = this
                that.$confirm('确定要删除选中的一条记录吗？注意本操作不可恢复', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    that.$emit('change',{tabIndex:that.tabIndex,index:e})
                    that.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                }).catch(() => {
                    that.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            },

            handleInput(e){
                this.$emit('edit',{tabIndex:this.tabIndex,i:e.target.dataset.i,j:e.target.dataset.j,value:e.target.value,type:2})

            }

        },
    }
</script>

<style scoped>
    .xx-common-item{
        margin: 20px 15px;
        border: 2px solid blue;
        display: flex;
        flex-wrap: wrap;
        position: relative;
    }
    .xx-common-item-c{
        width: 50%;
        box-sizing: border-box;
        border-right: 1px solid #c3c3c3;

        padding: 0 5px;
    }
    .xx-common-item-c-center{
        text-align: center;
    }
    .xx-common-item-i{
        width: calc(100%/3);
        height: 60px;
        line-height: 60px;
        display: flex;
        flex-direction: row;
        border-bottom: 1px solid #c3c3c3;
    }
    .xx-common-item-close{
        position: absolute;
        right:-15px;
        top:-15px;
        font-size: 20px;
    }
    .xx-common-item-i-50{
        width: 50%;
    }
    .xx-common-item-table{
        background-color: #CBE5FB;
        padding: 0 5px;
    }
    .xx-common-item-select{
        width: 90%;
    }
    .xx-common-item-input{
        width: 100%;
        height: 50px;
        outline:none;
        line-height: 60px;
        border: 0;
    }
    .xx-common-item-input:focus{
        outline: none;
    }
</style>