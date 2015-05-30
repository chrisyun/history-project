/**
 * @description 三态树节点UI
 * @author Wuqingming
 * @since 2010-09
 * @version 1.0
 */
Ext.ux.TreeCheckNodeUI = function() {
	// 多选: 'multiple'(默认)
    // 单选: 'single'
    // 级联多选: 'cascade'(同时选父和子);'parentCascade'(选父);'childCascade'(选子)
	this.checkModel = 'multiple';
	this.checkstree = new Array();
	this.onlyLeafCheckable = false;
	this.imgSrc = "../../resources/images/local/icons/";
	Ext.ux.TreeCheckNodeUI.superclass.constructor.apply(this, arguments);
};
Ext.extend(Ext.ux.TreeCheckNodeUI, Ext.tree.TreeNodeUI, {
	renderElements : function(n, a, targetNode, bulkRender) {
		var tree = n.getOwnerTree();
		this.checkModel = tree.checkModel || this.checkModel;
		this.imgSrc = tree.imgSrc || this.imgSrc;
		this.onlyLeafCheckable = tree.onlyLeafCheckable || false;
		this.indentMarkup = n.parentNode ? n.parentNode.ui.getChildIndent() : '';
		var cb = (!this.onlyLeafCheckable || a.leaf);
		var href = a.href ? a.href : Ext.isGecko ? "" : "#";
		var forderLeafImgUrl = "";
		var forderLeafImgClass = "";
		if ( Ext.isEmpty(n.attributes.icon) ) {
			forderLeafImgUrl = this.emptyIcon;
			forderLeafImgClass = "x-tree-node-icon";
		} else {
			forderLeafImgUrl = this.imgSrc + n.attributes.icon;
		}
		var buf = [
			'<li class="x-tree-node"><div ext:tree-node-id="',n.id,
			'" class="x-tree-node-el x-tree-node-leaf x-unselectable ', a.cls,'" unselectable="on">',
   			'<span class="x-tree-node-indent">',this.indentMarkup,"</span>",
    		'<img src="', this.emptyIcon, '" class="x-tree-ec-icon x-tree-elbow" />',
    		'<img src="', a.icon || this.emptyIcon, '" class="x-tree-node-icon',
    			(a.icon ? " x-tree-node-inline-icon" : ""),(a.iconCls ? " "+a.iconCls : ""),'" unselectable="on" />',
    		'<img class="x-tree-node-cb" src="' + this.imgSrc + "none" + '.gif" style="vertical-align: top;margin: 1 1 5 1px;"/>',
    		'<a hidefocus="on" class="x-tree-node-anchor" href="',href,'" tabIndex="1" ',
     			a.hrefTarget ? ' target="'+a.hrefTarget+'"' : "", '><span unselectable="on">',n.text,"</span></a></div>",
    		'<ul class="x-tree-node-ct" style="display:none;"></ul>',
    		"</li>"
    	].join('');
    	
    	/**
		var buf = [
            '<li class="x-tree-node"><div ext:tree-node-id="',
            n.id,
            '" class="x-tree-node-el x-tree-node-leaf x-unselectable ',
            a.cls,
            '" unselectable="on">',
            '<span class="x-tree-node-indent">',
            this.indentMarkup,
            "</span>",
            '<img src="',
            this.emptyIcon,
            '" class="x-tree-ec-icon x-tree-elbow" />',
			'<img src="',
				forderLeafImgUrl, //,
			'" class="', forderLeafImgClass, '"/>',
			'<img class="x-tree-node-cb" src="' + this.imgSrc
					+ "none" + '.gif"/>',
            '<a hidefocus="on" class="x-tree-node-anchor" href="',
            href,
            '" tabIndex="1" ',
            a.hrefTarget ? ' target="' + a.hrefTarget + '"' : "",
            '><span unselectable="on">',
            n.text,
            "</span></a></div>",
            '<ul class="x-tree-node-ct" style="display:none;"></ul>',
            "</li>"
       	].join('');*/
		var nel;
        if(bulkRender !== true && n.nextSibling && (nel = n.nextSibling.ui.getEl())){
            this.wrap = Ext.DomHelper.insertHtml("beforeBegin", nel, buf);
        }else{
            this.wrap = Ext.DomHelper.insertHtml("beforeEnd", targetNode, buf);
        }
		this.elNode = this.wrap.childNodes[0];
		this.ctNode = this.wrap.childNodes[1];
		var cs = this.elNode.childNodes;
		this.indentNode = cs[0];
		this.ecNode = cs[1];
		var index = 3;
		if (cb) {
			this.checkbox = cs[3];
			Ext.fly(this.checkbox).on('click', this.onCheck.createDelegate(this, [null]));
			index++;
		}
		this.anchor = cs[index];
		this.textNode = cs[index].firstChild;
	},

	onCheck : function() {
		this.check(this.toggleCheck(this.node.attributes.checked));
	},
	check : function(checked) {
		var n = this.node;
		n.attributes.checked = checked;
		this.setNodeIcon(n);
		if (this.checkModel == 'cascade' || this.checkModel == 'multiple') {
			this.childCheck(n, n.attributes.checked);
			this.parentCheck(n);
		} else if (this.checkModel == 'parentCascade') {
			this.parentCheck(n);
		} else if (this.checkModel == 'childCascade') {
			this.childCheck(n, n.attributes.checked);
		} 

	},
	parentCheck : function(node) {
		var currentNode = node;
		while ((currentNode = currentNode.parentNode) != null) {
			if (!currentNode.getUI().checkbox)
				continue;
			var part = false;
			var sel = 0;
			Ext.each(currentNode.childNodes, function(child) {
						if (child.attributes.checked == 'all')
							sel++;
						else if (child.attributes.checked == 'part') {
							part = true;
							return false;
						}
					});
			if (part)
				currentNode.attributes.checked = 'part';
			else {
				var selType = null;
				if (sel == currentNode.childNodes.length) {
					currentNode.attributes.checked = 'all';
				} else if (sel == 0) {
					currentNode.attributes.checked = 'none';
				} else {
					currentNode.attributes.checked = 'part';
				}
			}
			this.setNodeIcon(currentNode);
		};
	},
	setNodeIcon : function(n) {
		if (n.getUI() && n.getUI().checkbox)
			n.getUI().checkbox.src = this.imgSrc + n.attributes.checked
					+ '.gif';
	},

	childCheck : function(node, checked) {
		node.expand(false, false);
		if (node.childNodes)
			Ext.each(node.childNodes, function(child) {
						child.attributes.checked = checked;
						this.setNodeIcon(child);
						this.childCheck(child, checked);
					}, this);
	},
	toggleCheck : function(value) {
		return (value == 'all' || value == 'part') ? 'none' : 'all';
	},
	initCheckedNodes : function(node) {
		var nodes = node.childNodes;
		if (nodes.length != 0) {
			for (var i = 0; i < nodes.length; i++) {
				if (((nodes[i].attributes.checked == undefined)
						? "none"
						: (nodes[i].attributes.checked)) != 'none') {
					this.checkstree[this.checkstree.length] = nodes[i];
				}
				this.initCheckedNodes(nodes[i]);
			}
		}

	},
	getCheckedNodesResult : function() {
		return this.checkstree;
	},
	getCheckedNodes : function(node) {
		this.checkstree.length = 0;
		this.initCheckedNodes(node);
		return this.getCheckedNodesResult();
	},
	setChecked : function(n, mark) {
		if (mark)
			n.attributes.checked = "none";
		else
			n.attributes.checked = "all";
		if (n.getUI() && n.getUI().checkbox)
			n.getUI().checkbox.src = this.imgSrc + n.attributes.checked
					+ '.gif';
		n.getUI().onCheck();
	}

});