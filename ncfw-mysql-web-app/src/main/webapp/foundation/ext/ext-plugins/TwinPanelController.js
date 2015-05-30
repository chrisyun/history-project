/**
 * @description 双向选择面板
 * @author Wuqingming
 * @since 2010-09
 * @version 1.0
 */
Ext.ux.TwinPanelController = function(config) {
  var config = config || {};
  var defaultConfig = {};
  Ext.apply(this, config, defaultConfig);

  this.addOneButton = new Ext.Button({
        xtype : 'button',
        handler : this.addOne,
        scope : this,
        tooltip : '添加',
        icon : sys.extPath + '/resources/images/default/grid/page-next.gif',
        cls : 'x-btn-icon'
  });

  this.addAllButton = new Ext.Button({
        xtype : 'button',
        handler : this.addAll,
        scope : this,
        tooltip : '添加所有',
        icon : sys.extPath + '/resources/images/default/grid/page-last.gif',
        cls : 'x-btn-icon'
  });

  this.removeOneButton = new Ext.Button({
        xtype : 'button',
        handler : this.removeOne,
        scope : this,
        tooltip : '移除',
        icon : sys.extPath + '/resources/images/default/grid/page-prev.gif',
        cls : 'x-btn-icon'
  });

  this.removeAllButton = new Ext.Button({
        xtype : 'button',
        handler : this.removeAll,
        scope : this,
        tooltip : '移除所有',
        icon : sys.extPath + '/resources/images/default/grid/page-first.gif',
        cls : 'x-btn-icon'
  });

  Ext.ux.TwinPanelController.superclass.constructor.call(this, {
        layout : 'table',
        style : 'padding-top: ' + (this.doubleHeight / 3) + 'px; padding-right: 10px; padding-left: 10px',
        width : 45,
        defaults : {
          style : 'margin-bottom: 3px'
        },
        layoutConfig : {
          columns : 1
        },
        items : [this.addOneButton, this.addAllButton, this.removeOneButton, this.removeAllButton]
      });

};

Ext.extend(Ext.ux.TwinPanelController, Ext.Panel, {
      disable : function() {
        this.addOneButton.disable();
        this.addAllButton.disable();
        this.removeOneButton.disable();
        this.removeAllButton.disable();
      },
      enable : function() {
        this.addOneButton.enable();
        this.addAllButton.enable();
        this.removeOneButton.enable();
        this.removeAllButton.enable();
      },
      addOne : function() {
        this.moveItems(0, 2, false);
      },

      addAll : function() {
        this.moveItems(0, 2, true);
      },

      removeOne : function() {
        this.moveItems(2, 0, false);
      },

      removeAll : function() {
        this.moveItems(2, 0, true);
      },

      moveItems : function(fromIndex, toIndex, moveAll) {
        var fromPanel = this.ownerCt.getComponent(fromIndex);
        var toPanel = this.ownerCt.getComponent(toIndex);

        var dragZone = fromPanel.dragZone;
        var dropZone = toPanel.dropZone;
        var fn = toPanel.dropConfig.onContainerOver.createDelegate(dropZone, [dragZone, null], 0);
        var checkIfDragAllowed = function(node) {
          return (!node.disabled) && fn({
                node : node
              }) == dropZone.dropAllowed;
        }

        if (fromPanel && toPanel)
        {
          if (toPanel.sorter && toPanel.sorter.disableSort)
          {
            toPanel.sorter.disableSort(toPanel);
          }
          if (fromPanel.sorter && fromPanel.sorter.disableSort)
          {
            fromPanel.sorter.disableSort(fromPanel);
          }
          var fromRoot = fromPanel.root;
          var toRoot = toPanel.root;
          if (moveAll)
          {
            for (var i = 0; i < fromRoot.childNodes.length; i++)
            {
              var node = fromRoot.childNodes[i];
              if (checkIfDragAllowed(node))
              {
                toRoot.appendChild(node);
                i--;
              }
            }
          }
          else
          {
            var selectedNodes = fromPanel.getSelectionModel().getSelectedNodes();
            if (selectedNodes)
            {
              for (var i = 0; i < selectedNodes.length; i++)
              {
                var node = selectedNodes[i];
                if (checkIfDragAllowed(node))
                {
                  toRoot.appendChild(node);
                  i--;
                }
              }
            }
          }
          if (toPanel.sorter && toPanel.sorter.enableSort)
          {
            toPanel.sorter.enableSort(toPanel);
          }
          if (fromPanel.sorter && fromPanel.sorter.enableSort)
          {
            fromPanel.sorter.enableSort(fromPanel);
          }
        }
      }
    });

Ext.reg('twinpanelcontroller', Ext.ux.TwinPanelController);

Ext.ux.TwinPanelChooser = function(config) {
  var config = config || {};
  var defaultConfig = {
    doubleWide : false,
    displayField : 'name',
    validateRightItems : false,
    validateRightItemsText : 'Invalid items selected.',
    nodeIcon : sys.extPath + '/resources/images/default/tree/leaf.gif'
  };
  Ext.apply(this, config, defaultConfig);

  Ext.ux.TwinPanelChooser.superclass.constructor.call(this, {
        layout : 'column',
        autoHeight : true,
        style : 'padding: 10px 0 10px 0',
        listeners : {
          beforedestroy : {
            fn : function() {
              if (this.store)
              {
                this.loadStore();
                this.store.un('load', this.loadStore, this);
              }
            },
            scope : this
          }
        },

        items : [{
              xtype : 'multiselecttreepanel',
              name : 'sourcetree',
              // id: id + '_staging-profiles-available-groups-tree', //note:
              // unique ID is assinged before instantiation
              title : this.titleLeft,
              border : true, // note: this seem to have no effect w/in form
                              // panel
              bodyBorder : true, // note: this seem to have no effect w/in form
                                  // panel
              // note: this style matches the expected behavior
              bodyStyle : 'background-color:#FFFFFF; border: 1px solid #B5B8C8',
              width : this.doubleWidth,
              height : this.doubleHeight,
              animate : true,
              lines : false,
              autoScroll : true,
              containerScroll : true,
              // @note: root node must be instantiated uniquely for each
              // instance of treepanel
              // @ext: can TreeNode be registerd as a component with an xtype so
              // this new root node
              // may be instantiated uniquely for each form panel that uses this
              // config?
              rootVisible : false,
              root : new Ext.tree.TreeNode({
                    text : 'root'
                  }),
              enableDD : true,
              ddScroll : true,
              dropConfig : {
                allowContainerDrop : true,
                onContainerDrop : function(source, e, data) {
                  if (data.nodes)
                  {
                    for (var i = 0; i < data.nodes.length; i++)
                    {
                      this.tree.root.appendChild(data.nodes[i]);
                    }
                  }
                  return true;
                },
                onContainerOver : function(source, e, data) {
                  return this.dropAllowed;
                },
                // passign padding to make whole treePanel the drop zone. This
                // is dependent
                // on a sonatype fix in the Ext.dd.DropTarget class. This is
                // necessary
                // because treepanel.dropZone.setPadding is never available in
                // time to be useful.
                padding : [0, 0, (this.halfSize ? 124 : 274), 0]
              }
            }, {
              xtype : 'twinpanelcontroller',
              name : 'twinpanel',
              doubleHeight : this.doubleHeight
            }, {
              xtype : 'multiselecttreepanel',
              name : 'targettree',
              // id: '_staging-profiles-target-groups-tree', //note: unique ID
              // is assinged before instantiation
              title : this.titleRight,
              cls : this.required ? 'required-field' : null,
              border : true, // note: this seem to have no effect w/in form
                              // panel
              bodyBorder : true, // note: this seem to have no effect w/in form
                                  // panel
              // note: this style matches the expected behavior
              bodyStyle : 'background-color:#FFFFFF; border: 1px solid #B5B8C8',
              width : this.doubleWidth,
              height : this.doubleHeight + (Ext.isGecko ? this.geckoHeight || 75 : 0),
              animate : true,
              lines : false,
              autoScroll : true,
              containerScroll : true,
              // @note: root node must be instantiated uniquely for each
              // instance of treepanel
              // @ext: can TreeNode be registerd as a component with an xtype so
              // this new root node
              // may be instantiated uniquely for each form panel that uses this
              // config?
              rootVisible : false,
              root : new Ext.tree.TreeNode({
                    text : 'root'
                  }),
              enableDD : true,
              ddScroll : true,
              dropConfig : {
                allowContainerDrop : true,
                onContainerDrop : function(source, e, data) {
                  if (data.nodes)
                  {
                    for (var i = 0; i < data.nodes.length; i++)
                    {
                      this.tree.root.appendChild(data.nodes[i]);
                    }
                  }
                  return true;
                },
                onContainerOver : function(source, e, data) {
                  return this.dropAllowed;
                },
                // passign padding to make whole treePanel the drop zone. This
                // is dependent
                // on a sonatype fix in the Ext.dd.DropTarget class. This is
                // necessary
                // because treepanel.dropZone.setPadding is never available in
                // time to be useful.
                padding : [0, 0, (this.halfSize ? 124 : 274), 0]
              },
              // added Field values to simulate form field validation
              invalidText : (this.invalidText == null) ? 'Select one or more items' : this.invalidText,
              validate : function() {
                return (this.root.childNodes.length > 0);
              },
              invalid : false,
              listeners : {
                'append' : {
                  fn : function(tree, parentNode, insertedNode, i) {
                    this.clearInvalid();
                  },
                  scope : this
                },
                'remove' : {
                  fn : function(tree, parentNode, removedNode) {
                    if (tree.root.childNodes.length < 1 && this.required)
                    {
                      this.markTreeInvalid(tree, null);
                    }
                    else
                    {
                      this.clearInvalid();
                    }
                  },
                  scope : this
                }
              }
            }]
      });

  if (this.store)
  {
    this.loadStore();
    this.store.on('load', this.loadStore, this);
  }
};

Ext.extend(Ext.ux.TwinPanelChooser, Ext.Panel, {
      disable : function() {
        this.find('name', 'twinpanel')[0].disable();
        this.find('name', 'sourcetree')[0].dragZone.lock();
        this.find('name', 'targettree')[0].dragZone.lock();
      },
      enable : function() {
        this.find('name', 'twinpanel')[0].enable();
        this.find('name', 'sourcetree')[0].dragZone.unlock();
        this.find('name', 'targettree')[0].dragZone.unlock();
      },
      createNode : function(root, rec) {
        root.appendChild(
        	new Ext.tree.TreeNode({
          		id : rec.id,
          		text : rec.data[this.displayField],
	          	payload : rec,
	          	allowChildren : false,
	          	draggable : true,
	          	leaf : true,
	          	disabled : rec.data.readOnly,
	          	icon : this.nodeIcon
            }));
      },

      loadStore : function() {
        if (this.store)
        {
          var root = this.getComponent(0).root;
          while (root.lastChild)
          {
            root.removeChild(root.lastChild);
          }
          this.store.each(function(rec) {
                this.createNode(root, rec);
              }, this);
        }
      },

      clearInvalid : function() {
        var tree = this.getComponent(2);
        if (tree.invalid)
        {
          // remove error messaging
          tree.getEl().child('.x-panel-body').setStyle({
                'background-color' : '#FFFFFF',
                border : '1px solid #B5B8C8'
              });
          Ext.form.Field.msgFx['normal'].hide(tree.errorEl, tree);
        }
      },

      markTreeInvalid : function(tree, errortext) {
        if (tree == null)
        {
          tree = this.getComponent(2);
        }
        var elp = tree.getEl();

        if (elp)
        {
          if (!tree.errorEl)
          {
            tree.errorEl = elp.createChild({
                  cls : 'x-form-invalid-msg'
                });
            tree.errorEl.setWidth(elp.getWidth(true)); // note removed -20 like
                                                        // on form fields
          }
          tree.invalid = true;
          var oldErrorText = tree.invalidText;
          if (errortext)
          {
            tree.invalidText = errortext;
          }
          tree.errorEl.update(tree.invalidText);
          tree.invalidText = oldErrorText;
          elp.child('.x-panel-body').setStyle({
                'background-color' : '#fee',
                border : '1px solid #dd7870'
              });
          Ext.form.Field.msgFx['normal'].show(tree.errorEl, tree);
        }
      },

      validate : function() {
        var rightTree = this.getComponent(2);

        if (this.containsInvalidNode())
        {
          this.markTreeInvalid(rightTree, this.validateRightItemsText);
          return false;
        }

        if (!this.required)
          return true;

        var valid = rightTree.validate.call(leftTree);
        if (!valid)
        {
          this.markTreeInvalid(rightTree, null);
        }
        return valid;
      },

      containsInvalidNode : function() {
        if (!this.validateRightItems)
        {
          return false;
        }
        var root = this.getComponent(2).root;

        var nodes = root.childNodes;

        for (var i = 0; i < nodes.length; i++)
        {
          if (nodes[i].attributes.payload.invalid == true)
          {
            return true;
          }
        }

        return false;
      },

      getValue : function() {
        var output = [];
        var nodes = this.getComponent(2).root.childNodes;

        for (var i = 0; i < nodes.length; i++)
        {
          if (!nodes[i].disabled)
          {
            output.push(this.valueField ? nodes[i].attributes.payload.data[this.valueField] : nodes[i].attributes.payload.data);
          }
        }

        return output;
      },
      
      getDisplayValue : function() {
        var output = [];
        var nodes = this.getComponent(2).root.childNodes;

        for (var i = 0; i < nodes.length; i++)
        {
          if (!nodes[i].disabled)
          {
            output.push(this.displayField ? nodes[i].attributes.payload.data[this.displayField] : nodes[i].attributes.payload.data);
          }
        }

        return output;
      },

      setValue : function(arr) {
        if (!Ext.isArray(arr))
        {
          arr = [arr];
        }
        var rightRoot = this.getComponent(2).root;
        while (rightRoot.lastChild)
        {
          rightRoot.removeChild(rightRoot.lastChild);
        }

        var leftRoot = this.getComponent(0).root;
        this.loadStore();
        var nodes = leftRoot.childNodes;
		if (arr[0] == null) {
			return;
		}
        for (var i = 0; i < arr.length; i++)
        {
          var valueId = arr[i];
          var name = valueId;
          var readOnly = false;
          if (typeof(valueId) != 'string')
          {
            name = valueId[this.displayField];
            readOnly = valueId.readOnly;
            valueId = valueId[this.valueField];
          }
          var found = false;
          for (var j = 0; j < nodes.length; j++)
          {
            var node = nodes[j];
            var nodeValue = this.valueField ? node.attributes.payload.data[this.valueField] : node.attributes.payload.id;
            if (nodeValue == valueId)
            {
              rightRoot.appendChild(node);
              if (readOnly)
              {
                node.disable();
              }
              found = true;
              break;
            }
          }
          if (!found)
          {
            var rec = {
              id : valueId,
              data : {
                readOnly : readOnly
              }
            };
            rec.data[this.valueField] = valueId;
            var displayValue = name;
            if (this.validateRightItems)
            {
              displayValue += ' - (Invalid)';
            }
            rec.data[this.displayField] = displayValue;
            rec.invalid = true;
            this.createNode(rightRoot, rec);
          }
        }

        this.doLayout();
      }
    });

Ext.reg('twinpanelchooser', Ext.ux.TwinPanelChooser);
