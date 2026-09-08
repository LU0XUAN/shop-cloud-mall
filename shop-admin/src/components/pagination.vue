<template>
    <div style="margin-top: 20px;display: flex;justify-content: center">
<!--                      :hide-on-single-page="true"  当一整页能装下就隐藏-->
        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pageParams.currentPage"
                :page-size="pageParams.pagesize"
                :page-sizes="pageSizes"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
                background="true"
        ></el-pagination>
    </div>
</template>

<script setup>
    import { ref, reactive, defineEmits, defineProps } from 'vue';
    //父组件点击事件
    const emits  = defineEmits(['pageChange']);

    //接收父组件的值
    const props = defineProps({
        total: {
            type: Number,
            default: ''
        },
    })

    //变量
    const pageParams = reactive({
        currentPage: 1,
        pageSize: 10,
    })

    const pageSizes = ref([10,20,30,100]);

    //改变每页展示的数据条数
    const handleSizeChange = (val)=>{
        pageParams.pageSize = val;
        emits('pageChange', pageParams);
    }
    //改变当前页的页码
    const handleCurrentChange = (val)=>{
        pageParams.currentPage = val;
        emits('pageChange', pageParams);
    }


</script>

<style scoped>

</style>