export default class CustomPalette {
    constructor(create, elementFactory, palette, translate) {
        this.create = create;
        this.elementFactory = elementFactory;
        this.translate = translate;
        palette.registerProvider(this);
    }

    getPaletteEntries() {
        const {
            create,
            elementFactory,
            translate
        } = this;

        function createServiceTask(event) {
            const shape = elementFactory.createShape({ type: 'bpmn:UserTask' });
            create.start(event, shape);
        }
        function createCallActivity(event) {
            const shape = elementFactory.createShape({ type: 'bpmn:CallActivity' });
            create.start(event, shape);
        }
        //并行网关
        function createParallelGateway(event) {
          const shape = elementFactory.createShape({ type: 'bpmn:ParallelGateway' });
          create.start(event, shape);
        }
        //排他网关
        function createExclusiveGateway(event) {
          const shape = elementFactory.createShape({ type: 'bpmn:ExclusiveGateway' });
          create.start(event, shape);
        }
        return {
            'create.user-task': {
                group: 'activity',
                className: 'bpmn-icon-user-task',
                title: translate('Create UserTask'),
                action: {
                    dragstart: createServiceTask,
                    click: createServiceTask
                }
            },
            'create.call-activity': {
                group: 'activity',
                className: 'bpmn-icon-call-activity',
                title: '创建内嵌流程',
                action: {
                    dragstart: createCallActivity,
                    click: createCallActivity
                }
            },
            'create.parallel-gateway': {
              title: '创建并行网关',    // 鼠标悬浮到节点上显示的文字
              className: 'bpmn-icon-gateway-parallel',   // 样式名
              action: {      // 操作该节点时会触发的事件，此时只注册一个拖动事件即可，否则拖动时没有效果
                dragstart: createParallelGateway,
                click: createParallelGateway
              }
            },
            'create.exclusive-gateway': {
              title: '创建排他网关',    // 鼠标悬浮到节点上显示的文字
              className: 'bpmn-icon-gateway-xor',   // 样式名
              action: {      // 操作该节点时会触发的事件，此时只注册一个拖动事件即可，否则拖动时没有效果
                dragstart: createExclusiveGateway,
                click: createExclusiveGateway
              }
            }
        };
    }
}

CustomPalette.$inject = [
    'create',
    'elementFactory',
    'palette',
    'translate'
];
