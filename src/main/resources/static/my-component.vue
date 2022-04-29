<template>

<div class="container" >
	<div class="row" >
		<div class="tree" >
			<ul >
				<li v-for="(item,index) in list " :key="index">
					<a v-if=" item.name !== '' " href="#">
                      <el-tooltip placement="top">
                        <div slot="content">哈希值：{{item.name}}</div>
                        <span  :style="{'background-color': item.color }">{{item.tag}}{{item.subName}}</span>
                      </el-tooltip>
					 <span>Index👉{{item.id}}</span>
					</a>
						<my-component :list="item.children" v-if=" item.name !== '' &&  item.children.length !== 0  "></my-component>
				</li>
			</ul>
		</div>
	</div>
</div>




</template>

<script>
module.exports = {
  name: "my-component",
  props: {
    list: Array,
  },
};
</script>
<style>
ul {
  padding-left: 20px !important;
}
</style>