<template>
  <div>
    <el-tree
      :data="deps"
      :props="defaultProps"
      :expand-on-click-node="false"
      :filter-node-method="filterNode"
      ref="tree"
    >
      <span
        class="custom-tree-node"
        style="display: flex;justify-content: space-between;width: 100%;"
        slot-scope="{ node, data }"
      >
        <span>{{ data.name }}</span>
        <span>
          <el-button
            type="primary"
            size="mini"
            class="depBtn"
            @click="() => showAddDepView(data)"
          >添加部门</el-button>
          <el-button type="danger" size="mini" class="depBtn" @click="() => deleteDep(data)">删除部门</el-button>
        </span>
      </span>
    </el-tree>

    <el-dialog title="添加部门" :visible.sync="dialogVisible" width="30%">
      <div>
        <table>
          <tr>
            <td>
              <el-tag>上级部门</el-tag>
            </td>
            <td>{{pname}}</td>
          </tr>
          <tr>
            <td>
              <el-tag>部门名称</el-tag>
            </td>
            <td>
              <el-input v-model="dep.name" placeholder="请输入部门名称..."></el-input>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddDep">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "Department",
  data() {
    return {
      deps: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      dialogVisible: false,
      pname: "",
      dep: {
        name: "",
        parentId: -1
      }
    };
  },
  mounted() {
    this.initDeps();
  },
  methods: {
    initDeps() {
      this.getRequest("/sys/department/").then(resp => {
        if (resp) {
          this.deps = resp;
        }
      });
    },
    addDep2Deps(deps, dep) {
      for (let i = 0; i < deps.length; i++) {
        let d = deps[i];
        if (d.id == dep.parentId) {
          d.children = d.children.concat(dep);
          if (d.children.length > 0) {
            d.parent = true;
          }
          return;
        } else {
          this.addDep2Deps(d.children, dep);
        }
      }
    },
    initDep() {
      this.dep = {
        name: "",
        parentId: -1
      };
      this.pname = "";
    },
    showAddDepView(data) {
      this.pname = data.name;
      this.dep.parentId = data.id;
      this.dialogVisible = true;
    },
    doAddDep() {
      this.postRequest("/sys/department/", this.dep).then(resp => {
        if (resp) {
          this.addDep2Deps(this.deps, resp.obj);
          this.dialogVisible = false;
          this.initDep();
        }
      });
    },
    removeDepFromDeps(p, deps, id) {
      for (let i = 0; i < deps.length; i++) {
        let d = deps[i];
        if (d.id == id) {
          deps.splice(i, 1); //从deps中删除
          if (deps.length == 0) {
            p.parent = false;
          }
          return;
        } else {
          this.removeDepFromDeps(d, d.children, id);
        }
      }
    },
    deleteDep(data) {
      if (data.parent) {
        this.$message.error("父部门删除失败");
      } else {
        this.$confirm(
          "此操作将永久删除【" + data.name + "】部门, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        ).then(() => {
          this.deleteRequest("/sys/department/" + data.id).then(resp => {
            if (resp) {
              this.removeDepFromDeps(null, this.deps, data.id);
            }
          });
        });
      }
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    }
  }
};
</script>
<style scoped>
.depBtn {
  padding: 2px;
}
</style>