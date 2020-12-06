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
                    <el-input placeholder="请输入企业名称或者宗地号进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                              clearable
                              @clear="mainDataList"
                              style="width: 300px;margin-right: 10px" v-model="keyword"
                              @keydown.enter.native="mainDataList" :disabled="showAdvanceSearchView"></el-input>
                    <el-button icon="el-icon-search" type="primary" @click="mainDataList" :disabled="showAdvanceSearchView">
                        搜索
                    </el-button>

                    <el-button type="primary" @click="showAdvanceSearchView = !showAdvanceSearchView">
                        <i :class="showAdvanceSearchView?'fa fa-angle-double-up':'fa fa-angle-double-down'"
                           aria-hidden="true"></i>
                        高级搜索
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
            <transition name="slide-fade">
                <div v-show="showAdvanceSearchView"
                     style="border: 1px solid #409eff;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0px;">
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
                        <el-select v-model='searchValue.state' placeholder='***显示所有状态***' @change="mainDataList('advanced')" clearable>
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
                </el-row>
                    <el-row style="margin-top: 10px">
                        <el-col :span="8">
                            <el-input
                              clearable
                              @clear="mainDataList"
                              style="width: 100px;margin-right: 10px" v-model="searchValue.startArea"
                              @keydown.enter.native="mainDataList" />
                               ≤ 面积 ≤
                             <el-input
                              clearable
                              @clear="mainDataList"
                              style="width: 100px;margin-left: 10px" v-model="searchValue.endArea"
                              @keydown.enter.native="mainDataList" />
                        </el-col>
                        <el-col :span="8">
                             <el-input
                              clearable
                              @clear="mainDataList"
                              style="width: 100px;margin-right: 10px" v-model="searchValue.startLease"
                              @keydown.enter.native="mainDataList" />
                               ≤ 税收 ≤
                             <el-input
                              clearable
                              @clear="mainDataList"
                              style="width: 100px;margin-left: 10px" v-model="searchValue.endLease"
                              @keydown.enter.native="mainDataList" />
                        </el-col>
                        <el-col :span="6">
                            <el-input
                              clearable
                              @clear="mainDataList"
                              style="width: 100px;margin-right: 10px" v-model="searchValue.startYield"
                              @keydown.enter.native="mainDataList" />
                               ≤ 产值 ≤
                             <el-input
                              clearable
                              @clear="mainDataList"
                              style="width: 100px;margin-left: 10px" v-model="searchValue.endYield"
                              @keydown.enter.native="mainDataList" />
                        </el-col>

                    </el-row>
                    <el-row style="margin-top: 10px">
                        <el-col :span="8">
                            <el-input
                              clearable
                              @clear="mainDataList"
                              style="width: 100px;margin-right: 10px" v-model="searchValue.startBuildArea"
                              @keydown.enter.native="mainDataList" />
                               ≤ 建筑面积 ≤
                             <el-input
                              clearable
                              @clear="mainDataList"
                              style="width: 100px;margin-left: 10px" v-model="searchValue.endBuildArea"
                              @keydown.enter.native="mainDataList" />
                        </el-col>
                        <el-col :span="8">
                              <el-input
                              clearable
                              @clear="mainDataList"
                              style="width: 100px;margin-right: 10px" v-model="searchValue.startRatio"
                              @keydown.enter.native="mainDataList" />
                               ≤ 容积率 ≤
                             <el-input
                              clearable
                              @clear="mainDataList"
                              style="width: 100px;margin-left: 10px" v-model="searchValue.endRatio"
                              @keydown.enter.native="mainDataList" />
                        </el-col>
                          <el-col :span="6">
                              <el-input
                              clearable
                              @clear="mainDataList"
                              style="width: 100px;margin-right: 10px" v-model="searchValue.startRD"
                              @keydown.enter.native="mainDataList" />
                               ≤ R&D ≤
                             <el-input
                              clearable
                              @clear="mainDataList"
                              style="width: 100px;margin-left: 10px" v-model="searchValue.endRD"
                              @keydown.enter.native="mainDataList" />
                        </el-col>
                        </el-row>
                    <el-row style="margin-top: 15px; margin-left: 400px">
                        <el-col :span="8" :offset="4">
                            <el-button size="mini" icon="fa fa-reply-all" type="primary" @click="cancel()">  取消</el-button>
                            <el-button size="mini" icon="el-icon-search" type="primary" @click="mainDataList('advanced')">搜索</el-button>
                        </el-col>
                    </el-row>
                    </div>
            </transition>
       </div>
        <div style="margin-top: 10px">
            <el-table
                    ref="table"
                    :header-cell-style="{background:'#eef1f6',color:'#606266'}"
                    :row-key="getRowKeys"
                    :expand-row-keys="expands"
                    @expand-change="expandSelect"
                    :data="tableData"
                    :stripe=true
                    :border=true
                    v-loading="loading"
                    element-loading-text="正在加载..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    style="width: 100%">
                <!---------------------------- 列展开数据 start-------------------------------->
                 <el-table-column type="expand" fixed width="55">
                        <template slot-scope="scope">
                        <el-table
                            :header-cell-style="{background:'#cfe6ff',color:'#606266'}"
                            :data="scope.row.ruleItemData"
                            :stripe=true
                            :border=true
                            v-loading="columnloading"
                            element-loading-text="请稍候，正在加载..."
                            element-loading-spinner="el-icon-loading"
                            element-loading-background="rgba(0, 0, 0, 0.8)"
                            style="width: 100%">
                                <el-table-column
                                fixed
                                prop="parcelNum"
                                label="宗地号"
                                align="left"
                                width="120">
                                </el-table-column>
                                <el-table-column
                                        fixed
                                        prop="leaseName"
                                        width="95"
                                        align="left"
                                        label="租赁企业">
                                         <template scope="scope">
                                            <div style="color:blue;text-decoration:underline;cursor:pointer;" @click="getEnterprise(scope.row)">{{ scope.row.leaseName}}</div>
                                         </template>
                                </el-table-column>
                                 <el-table-column
                                        prop="area"
                                        label="面积（亩）"
                                        align="left"
                                        width="85">
                                </el-table-column>
                                <el-table-column
                                    prop="taxation"
                                    width="95"
                                    align="left"
                                    label="税收（万元）">
                                </el-table-column>
                                <el-table-column
                                    prop="industryPro"
                                    width="150"
                                    align="left"
                                    label="产值（万元）">
                                </el-table-column>
                                <el-table-column
                                    prop="leaseDate"
                                    width="150"
                                    align="left"
                                    label="租赁日期">
                                </el-table-column>
                                <el-table-column
                                    prop="leaseStartDate"
                                    width="150"
                                    align="left"
                                    label="租期">
                                </el-table-column>
                                 <el-table-column
                                    prop="RD"
                                    width="80"
                                    label="R&D占比">
                                </el-table-column>
                                <el-table-column
                                    prop="scale"
                                    width="100"
                                    label="规模">
                                </el-table-column>
                                <el-table-column
                                    prop="netWork"
                                    width="180"
                                    align="left"
                                    label="网格">
                                </el-table-column>
                                <el-table-column
                                    prop="state"
                                    width="100"
                                    align="left"
                                    label="状态">
                                </el-table-column>
                                  <el-table-column
                                        prop="type"
                                        width="220"
                                        align="left"
                                        label="企业类别">
                                </el-table-column>
                        </el-table>
                        </template>
                </el-table-column>
                <!---------------------------- 列展开数据 end-------------------------------->
                <el-table-column
                        prop="parcelNum"
                        fixed
                        align="left"
                        label="宗地号"
                        width="120">
                    <template scope="scope">
                        <div style="color:blue;text-decoration:underline;cursor:pointer;" @click="getMore2(scope.row)">{{ scope.row.parcelNum }}</div>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="enterPriseName"
                        label="主体企业"
                        align="left"
                        width="100">
                    <template scope="scope">
                        <div style="color:blue;text-decoration:underline;cursor:pointer;" @click="getEnterprise(scope.row)">{{ scope.row.enterPriseName }}</div>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="area"
                        label="面积（亩）"
                        align="left"
                        width="85">
                </el-table-column>
                <el-table-column
                        prop="taxation"
                        width="95"
                        align="left"
                        label="税收（万元）">
                </el-table-column>
                <el-table-column
                        prop="industryPro"
                        width="150"
                        align="left"
                        label="产值（万元）">
                </el-table-column>
                <!-- <el-table-column
                        prop="buildArea"
                        width="70"
                        label="建筑面积">
                </el-table-column> -->
                <!-- <el-table-column
                        prop="plotRatio"
                        width="100"
                        label="容积率">
                </el-table-column> -->
                <el-table-column
                        prop="RD"
                        width="80"
                        label="R&D占比">
                </el-table-column>
                <el-table-column
                        prop="scale"
                        width="100"
                        label="规模">
                </el-table-column>
                <el-table-column
                        prop="netWork"
                        width="180"
                        align="left"
                        label="网格">
                </el-table-column>
                <el-table-column
                        prop="state"
                        width="100"
                        align="left"
                        label="状态">
                </el-table-column>
                <el-table-column
                        prop="type"
                        width="220"
                        align="left"
                        label="企业类别">
                </el-table-column>
                <!-- <el-table-column
                        fixed="right"
                        width="200"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button @click="showEditEmpView(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
                        <el-button style="padding: 3px" size="mini" type="primary">查看高级资料</el-button>
                        <el-button @click="deleteEmp(scope.row)" style="padding: 3px" size="mini" type="danger">删除
                        </el-button>
                    </template>
                </el-table-column> -->
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
    <!-- 土地详情 dialog -------------------------------------start----------------------------------- -->
         <el-dialog
                :title="title"
                :visible.sync="dialogVisible"
                width="60%">
            <div>
                <span class="spanShow"> {{emp.parcelNum}} </span>
                <div class="firstTable">
                    <table align="center" valign="center">
                        <tr>
                            <td>
                                主体企业：
                            </td>
                            <td width="50%">
                                <span class="dataShow">{{emp.enterPriseName}}</span>
                            </td>

                            <td >
                                统一社会信用代码：
                            </td>
                            <td>
                             <span>{{emp.enterpriseCode}}</span>
                            </td>
                        </tr>
                            <tr>
                            <td>
                                宗地号：
                            </td>
                            <td>
                               <span >{{emp.parcelNum}}</span>
                            </td>

                            <td>
                                面积（亩）：
                            </td>
                            <td>
                                <span >{{emp.area}}</span>
                            </td>
                        </tr>

                        <tr>
                            <td >
                                容积率：
                            </td>
                            <td>
                                <span >{{emp.ratio}}</span>
                            </td>

                            <td>
                                建筑面积（平方米）：
                            </td>
                            <td>
                                <span >{{emp.buildArea}}</span>
                            </td>
                        </tr>

                    </table>
                </div>
                <div class="secondTable">
                <div class="zulin">
                    <span class="spanShow"> 租赁关系 </span>
                </div>
                    <table align="center" valign="center"  >
                        <tr>
                            <td>
                                租赁企业：
                            </td>

                            <td width="40%">
                                  <span class="dataShow">{{emp.leaseName}}</span>
                            </td>

                            <td >
                                统一社会信用代码：
                            </td>
                            <td>
                                 <span >{{emp.leaseCode}}</span>
                            </td>
                        </tr>
                            <tr>
                            <td>
                               租赁面积（亩）：
                            </td>
                            <td>
                                 <span >{{emp.leaseArea}}</span>
                            </td>

                            <td>
                               租赁时间：
                            </td>
                            <td>
                                 <span >{{emp.leaseDate}}</span>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
         </span>
     </el-dialog>
         <!-- 土地详情 dialog -------------------------------------end----------------------------------- -->

        <!-- 主体企业详情 dialog -------------------------------------start----------------------------------- -->
        <el-dialog
                :title="title2"
                :visible.sync="qiyeDialogVisible"
                width="70%">
            <div>
                <span class="spanShow"> {{enterprise.parcelNum}} </span>
                <div class="firstTable">
                    <table align="center" valign="center">
                        <tr>
                            <td>
                                主体企业：
                            </td>
                            <td width="37%">
                                <span class="dataShow">{{enterprise.enterPriseName}}</span>
                            </td>

                            <td >
                                税收（万元）：
                            </td>
                            <td>
                               <span>{{enterprise.taxation}}</span>
                            </td>
                        </tr>
                            <tr>
                            <td>
                                统一社会信用代码：
                            </td>
                            <td>
                               <span>{{enterprise.enterpriseCode}}</span>
                            </td>

                            <td>
                                产值：
                            </td>
                            <td>
                               <span>{{enterprise.industryPro}}</span>
                            </td>
                        </tr>

                        <tr>
                            <td >
                                联系人（法人）：
                            </td>
                            <td>
                               <span>{{enterprise.contact}}</span>
                            </td>
                            <td>
                                R&D占比：
                            </td>
                            <td>
                               <span>{{enterprise.RD}}</span>
                            </td>
                        </tr>

                        <tr>
                            <td >
                                联系电话：
                            </td>
                            <td>
                               <span>{{enterprise.contactNum}}</span>
                            </td>
                            <td>
                                面积：
                            </td>
                            <td>
                               <span>{{enterprise.leaseArea}}</span>
                            </td>
                        </tr>

                        <tr>
                            <td >
                                地址：
                            </td>
                            <td>
                               <span>{{enterprise.address}}</span>
                            </td>
                            <td>
                                建筑面积（平方米）：
                            </td>
                            <td>
                               <span>{{enterprise.buildArea}}</span>
                            </td>
                        </tr>
                        <tr>
                            <td >
                                网格：
                            </td>
                            <td>
                               <span>{{enterprise.netWork}}</span>
                            </td>
                            <td>
                                容积率：
                            </td>
                            <td>
                               <span></span>
                            </td>
                        </tr>

                        <tr>
                            <td >
                                状态：
                            </td>
                            <td>
                               <span>{{enterprise.state}}</span>
                            </td>
                            <td>
                                宗地号：
                            </td>
                            <td>
                               <span>{{enterprise.parcelNum}}</span>
                            </td>
                        </tr>

                        <tr>
                            <td >
                                企业类别：
                            </td>
                            <td>
                               <span>{{enterprise.type}}</span>
                            </td>

                        </tr>
                           <tr>
                            <td >
                                规模：
                            </td>
                            <td>
                               <span>{{enterprise.scale}}</span>
                            </td>
                            <td>
                                备注：
                            </td>
                            <td>
                               <span>{{enterprise.remark}}</span>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
            <el-button @click="qiyeDialogVisible = false">取 消</el-button>
         </span>
     </el-dialog>
        <!-- 企业详情 dialog -------------------------------------end----------------------------------- -->
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
        name: "MainDataList", //  综合数据列表
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
                    startArea: null,
                    endArea:null,
                    startLease:null,
                    endLease:null,
                    startYield:null,
                    endYield:null,
                    startBuildArea:null,
                    endBuildArea:null,
                    startRatio:null,
                    endRatio:null,
                    startRD:null,
                    endRD:null
                    },
                    scales: [{
                        value: '规上企业',
                        label: '规上企业'
                    }, {
                        value: '规下企业',
                        label: '规下企业'
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
                    expands: [],
                    getRowKeys(row) {
                        return row.id
                    },
                    title: '土地详情信息',
                    title2:'主体企业详情信息',
                    showAdvanceSearchView: false,
                    allDeps: [],
                    emps: [],
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
                    nations: [],
                    emp: {
                        enterPriseName:"",
                        enterpriseCode:"",
                        parcelNum: "",
                        area:"",
                        ratio:"",// 容积率
                        buildArea:"",//建筑面积
                        leaseName:"",//租赁企业
                        leaseCode:"",//租赁企业编码
                        leaseArea:"",//租赁面积
                        leaseDate:""//租赁日期
                    },
                    enterprise:{
                        enterPriseName:"",
                        enterpriseCode:"",
                        contact:"",// 联系人
                        contactNum:"",//联系电话
                        address:"",//地址
                        netWork:"",//网格
                        state:"",//状态
                        type:"",//企业类别
                        scale:"",//企业规模
                        taxation:"",// 税收
                        industryPro:"", //产值
                        RD:"",
                        leaseArea:"", //面积
                        buildArea:"",//建筑面积
                        ratio:"",//容积率
                    },
                    rules: {

                    }
                }
        },
        created() {
            this.mainDataList();
        },

        mounted() {
            // this.mainDataList();
            // this.initData();
            // this.initPositions();
        },
     methods: {
         getMore2(row) {
                this.dialogVisible = true;
                if(row.parcelNum){
                    this.emp.parcelNum = row.parcelNum;
                }
                if(row.enterPriseName){
                    this.emp.enterPriseName = row.enterPriseName
                }
                if(row.enterpriseCode){
                    this.emp.enterpriseCode = row.enterpriseCode
                }
                if(row.area){
                    this.emp.area = row.area
                }
                if(row.ratio){
                    this.emp.ratio = row.ratio
                }
                if(row.buildArea){
                    this.emp.buildArea = row.area
                }
                if(row.leaseName){
                    this.emp.leaseName = row.leaseName
                }

                if(row.leaseCode){
                    this.emp.leaseCode = row.leaseCode
                }
                if(row.leaseArea){
                    this.emp.leaseArea = row.leaseArea
                }
                if(row.leaseDate){
                    this.emp.leaseDate = row.leaseDate
                }

             this.getB = true;
             this.row = row
           },

         getEnterprise(row){
            this.qiyeDialogVisible = true;
            if(row.parcelNum){
                    this.enterprise.parcelNum = row.parcelNum;
                }
                if(row.enterPriseName){
                    this.enterprise.enterPriseName = row.enterPriseName
                }
                if(row.enterpriseCode){
                    this.enterprise.enterpriseCode = row.enterpriseCode
                }
                if(row.industryPro){
                    this.enterprise.industryPro = row.industryPro
                }
                if(row.contact){
                    this.enterprise.contact = row.contact
                }
                if(row.RD){
                    this.enterprise.RD = row.RD
                }
                if(row.contactNum){
                    this.enterprise.contactNum = row.contactNum
                }
                 if(row.address){
                    this.enterprise.address = row.address
                }
                if(row.area){
                    this.enterprise.buildArea = row.area
                }
                if(row.leaseArea){
                    this.enterprise.leaseArea = row.leaseArea
                }
                if(row.netWork){
                    this.enterprise.netWork = row.netWork
                }

                if(row.state){
                    this.enterprise.state = row.state
                }
                if(row.parcelNum){
                    this.enterprise.parcelNum = row.parcelNum
                }
                if(row.scale){
                    this.enterprise.scale = row.scale
                }
                if(row.type){
                    this.enterprise.type = row.type
                }
                 if(row.remark){
                    this.enterprise.remark = row.remark
                }
            this.getB = true;
             this.row = row
         },
        handleCurrentChange(val) {
            this.cur_page = val;
            this.getTableData();
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
                     //给每行数据强制追加一个数据项
                    resp.info.data.map(item => {
                        item.ruleItemData = [];
                    });
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
                startArea:this.searchValue.startArea,
                endArea:this.searchValue.endArea,
                startLease:this.searchValue.startLease,
                endLease:this.searchValue.endLease,
                startYield:this.searchValue.startYield,
                endYield:this.searchValue.endYield,
                startBuildArea:this.searchValue.startBuildArea,
                endBuildArea:this.searchValue.endBuildArea,
                startRatio:this.searchValue.startRatio,
                endRatio:this.searchValue.endRatio,
                startRD:this.searchValue.startRD,
                endRD:this.searchValue.endRD
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
        // 折叠面板每次只能展开一行
        expandSelect(row, expandedRows) {
            var that = this
            if (expandedRows.length) {
                if (expandedRows.length > 0) {
                    this.getLeaseData(row.id);
                }
            } else {
                that.expands = []// 默认不展开
            }
        },
        trim(s) {
            return s.replace(/(^\s*)|(\s*$)/g, '');
        },
        // 根据主体企业查询租赁企业信息
        getLeaseData(Id){
           let url = this.$CST.MAIN_DATA_GET_LEASEDATA;
             let params;
            params = {
                id:Id,
                year:this.year,
                month:this.month
            }
            this.columnloading = true;
            this.getRequest(url, params).then(resp => {
                if (resp) {
                       this.tableData.forEach((temp, index) => {
                        // 找到当前点击的行，把动态获取到的数据赋值进去
                        if (temp.id === Id) {
                            this.tableData[index].ruleItemData = resp.info.data;
                        }
                    });
                }
                this.columnloading = false;
            }).catch((reason) => {
                 this.columnloading = false;
                this.$alert('综合数据列表查询失败：' + reason,'error')
            }).finally(() => {
                this.columnloading = false;
            })
             this.columnloading = false;
        },
        cancel(){
                this.showAdvanceSearchView = false;
             }
         }
    }
</script>
