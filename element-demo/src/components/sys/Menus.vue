<template>
  <div>
    <div>
      <el-button type="primary" size="small" icon="el-icon-plus" @click="showAddTop">新增一级菜单</el-button>
    </div>
    <div>
      <el-table
        :data="menuData"
        style="margin-top:20px;width: 100%;"
        row-key="id"
        border
        :tree-props="{children: 'children'}"
      >
        <el-table-column prop="name" label="菜单名" width="180"></el-table-column>
        <el-table-column prop="component" label="组件名" width="180"></el-table-column>
        <el-table-column prop="url" label="url" width="180"></el-table-column>
        <el-table-column prop="path" label="path" width="180"></el-table-column>
        <el-table-column prop="iconCls" label="icon" width="180"></el-table-column>
        <el-table-column label="enabled" width="100">
          <template slot-scope="scope">
            <el-tag type="success" size="small" v-if="scope.row.enabled">已启用</el-tag>
            <el-tag type="danger" size="small" v-else>未启用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button @click="showAdd(scope.row)" size="mini" type="text">添加子菜单</el-button>
            <el-button @click="showEdit(scope.row)" size="mini" type="text">编辑</el-button>
            <el-button @click="deleteMenu(scope.row)" size="mini" type="text">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex;justify-content: flex-end">
                <el-pagination
                        background
                        @current-change="currentChange"
                        layout="total,prev, pager, next"
                        :total="total">
                </el-pagination>
      </div>
    </div>
    <div>
      <el-dialog :title="title" :visible.sync="dialogFormVisible">
        <el-form :model="menu" :rules="rules" ref="menuForm">
          <el-form-item label="菜单名称" :label-width="formLabelWidth">
            <el-input v-model="menu.name" placeholder="请输入菜单名称"></el-input>
          </el-form-item>
          <el-form-item label="组件名称" :label-width="formLabelWidth">
            <el-input v-model="menu.component" placeholder="请输入组件名称"></el-input>
          </el-form-item>
          <el-form-item label="路径" :label-width="formLabelWidth">
            <el-input v-model="menu.path" placeholder="请输入路径"></el-input>
          </el-form-item>
          <el-form-item label="url" :label-width="formLabelWidth">
            <el-input v-model="menu.url" placeholder="请输入url"></el-input>
          </el-form-item>
          <el-form-item label="上级" :label-width="formLabelWidth">
            <el-input v-model="menu.parentId" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="图标" :label-width="formLabelWidth">
            <el-input v-model="menu.iconCls" placeholder="请输入图标类名"></el-input>
          </el-form-item>
          <el-form-item label="是否启用" :label-width="formLabelWidth">
            <el-switch v-model="menu.enabled"></el-switch>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="addMenu">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
export default {
  name: "Menus",
  data() {
    return {
      menuData: [],
      title: "",
      dialogFormVisible: false,
      menu: {
        id: null,
        name: "",
        component: "",
        url: "",
        path: "",
        parentId: 1,
        iconCls: "",
        enabled: true
      },
      parentName: "",
      rules: {
        name: [{ required: true, message: "请输入菜单名", trigger: "blur" }],
        component: [
          { required: true, message: "请输入组件名", trigger: "blur" }
        ],
        path: [{ required: true, message: "请输入路径", trigger: "blur" }],
      },
      formLabelWidth: "120px",
      total: 0, //总数
      page: 1, //页码
    };
  },
  mounted() {
    this.initMenuData();
  },
  methods: {
    initMenuData() {
      this.getRequest("/sys/menu/?page="+this.page).then(resp => {
        if (resp) {
          this.menuData = resp.data;
          this.total = resp.total;
        }
      });
    },
    addMenu() {
      if (this.menu.id) {
        this.$refs["menuForm"].validate(valid => {
          if (valid) {
            this.putRequest("/sys/menu/", this.menu).then(resp => {
              if (resp) {
                this.dialogFormVisible = false;
                this.initMenuData();
              }
            });
          }
        });
      } else {
        this.$refs["menuForm"].validate(valid => {
          if (valid) {
            this.postRequest("/sys/menu/", this.menu).then(resp => {
              if (resp) {
                this.dialogFormVisible = false;
                this.initMenuData();
              }
            });
          }
        });
      }
    },
    showAddTop() {
      this.resetMenu();
      this.title = "新增一级菜单";
      this.dialogFormVisible = true;
    },
    showAdd(row) {
      this.resetMenu();
      if (row.parentId == 1) {
        this.menu.parentId = row.id;
      } else {
        this.menu.parentId = row.parentId;
      }

      this.title = "新增子菜单";
      this.dialogFormVisible = true;
    },
    showEdit(row) {
      this.menu.id = row.id;
      this.menu.name = row.name;
      this.menu.component = row.component;
      this.menu.url = row.url;
      this.menu.path = row.path;
      this.menu.iconCls = row.iconCls;
      this.menu.enabled = row.enabled;
      this.menu.parentId = row.parentId;
      this.title = "编辑菜单";
      this.dialogFormVisible = true;
    },
    resetMenu() {
      this.menu = {
        id: null,
        name: "",
        component: "",
        url: "",
        path: "",
        parentId: 1,
        iconCls: "",
        enabled: true
      }
    },
    deleteMenu(row) {
      this.$confirm(
        "此操作将永久删除【" + row.name + "】菜单, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          this.deleteRequest("/sys/menu/" + row.id).then(resp => {
            if (resp) {
              this.initMenuData();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    //page改变时触发
    currentChange(val) {
      this.page = val;
      this.initMenuData();
    },
  }
};
</script>
<style scoped>
</style>