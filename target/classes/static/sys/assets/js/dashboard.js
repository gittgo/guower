~function ($) {

    $(".widget-drag").sortable({
        placeholder: "sort-highlight",
        connectWith: '.widget-drag',
        handle: '.box-header,.small-box-icon',
        forcePlaceholderSize: true,
        zIndex: 999999,
        helper: "clone",
        revert: true,
        opacity: 0.8,
        delay: 100,
        tolerance: "pointer"
    });

    var lineCanvas = $('#lineChart').get(0).getContext('2d');
    var doughnutCanvas = $('#doughnutChart').get(0).getContext('2d');
    var doughnutCanvas1 = $('#doughnutChart1').get(0).getContext('2d');

    //销售量走势图；
    $.get(getWebPath() + "bssorder/homeOrderStatisticsByDays", function (r) {
        var arr = r.orderStatistics30;
        
        var lineChartLables = [];
        var lineChartDatas = [];
        for (var i = 0; i < arr.length; ++i) {
            lineChartLables[i] = arr[i].createTime;
            lineChartDatas[i] = arr[i].nums;
        }

        var lineChart = new Chart(lineCanvas, {
            type: 'line',
            data: {
                //labels: ["09-01", "09-02", "09-03", "09-04", "09-05", "09-06"],
                // datasets: [{
                //     label: '销量统计',
                //     data: [1, 30, 2, 15, 45, 16],
                //     backgroundColor: 'rgba(35,153,199,1)',
                //     borderColor: 'rgba(35,153,199,1)',
                //     borderWidth: 1,
                //     fill: false
                // }]
                labels: lineChartLables,
                datasets: [{
                    label: '最近30天销量',
                    data: lineChartDatas ,
                    backgroundColor: 'rgba(35,153,199,1)',
                    borderColor: 'rgba(35,153,199,1)',
                    borderWidth: 1,
                    fill: false
                }]
            },
            options: {
                animation: {
                    easing: 'easeInOutCubic',
                },
                legend: {
                    display: true,
                    labels: {
                        boxWidth: 100,
                        usePointStyle: true
                    }
                }
            }
        });
    });


    //销售量对比图
    $.get(getWebPath() + "bssorder/homeOrderStatisticsByShopId", function (r) {
        var arr = r.orderStatisticsByShop;
        var doughnutLables = [];
        var doughnutDatas = [];
        for (var i = 0; i < arr.length; ++i) {
            doughnutLables[i] = arr[i].shopname;
            doughnutDatas[i] = arr[i].nums;
        }

        var doughnutChart = new Chart(doughnutCanvas, {
            type: 'doughnut',
            data: {
                datasets: [{
                    //data: [700, 500, 600, 300, 100, 400],
                    data: doughnutDatas,
                    backgroundColor: [
                        'rgba(255, 108, 96, 1)',
                        'rgba(255, 153, 78, 1)',
                        'rgba(245, 217, 74, 1)',
                        'rgba(40, 193, 110, 1)',
                        'rgba(255,225, 230, 1)',
                        'rgba(255,238, 216, 1)',
                        'rgba(254,246, 220, 1)',
                        'rgba(219,242, 242, 1)',
                        'rgba(214,237, 250, 1)',
                        'rgba(236,226, 255, 1)',
                        'rgba(29, 159, 189, 1)'
                    ],
                    //label: '浏览器使用'
                    label: '店铺销量统计'
                }],
                // labels: [
                //     'Chrome',
                //     'FireFox',
                //     'IE',
                //     'Safari',
                //     'Opera',
                //     'Navigator'
                // ]
                labels: doughnutLables
            },
            options: {
                animation: {
                    easing: 'easeOutBounce',
                },
                legend: {
                    display: true,
                    labels: {
                        boxWidth: 100,
                        usePointStyle: true
                    }
                }
            }
        })
    });

    //商品销售量对比图
    $.get(getWebPath() + "bssorder/homeProductStatisticsByTypeId", function (r) {
        var arr = r.ProductStatisticsByType;
        var doughnutLables = [];
        var doughnutDatas = [];
        for (var i = 0; i < arr.length; ++i) {
            doughnutLables[i] = arr[i].typename;
            doughnutDatas[i] = arr[i].nums;
        }

        var doughnutChart1 = new Chart(doughnutCanvas1, {
            type: 'doughnut',
            data: {
                datasets: [{
                    //data: [700, 500, 600, 300, 100, 400],
                    data: doughnutDatas,
                    backgroundColor: [
                        'rgba(255, 108, 96, 1)',
                        'rgba(255, 153, 78, 1)',
                        'rgba(245, 217, 74, 1)',
                        'rgba(40, 193, 110, 1)',
                        'rgba(255,225, 230, 1)',
                        'rgba(255,238, 216, 1)',
                        'rgba(254,246, 220, 1)',
                        'rgba(219,242, 242, 1)',
                        'rgba(214,237, 250, 1)',
                        'rgba(236,226, 255, 1)',
                        'rgba(29, 159, 189, 1)'
                    ],
                    //label: '浏览器使用'
                    label: '商品销量统计'
                }],
                // labels: [
                //     'Chrome',
                //     'FireFox',
                //     'IE',
                //     'Safari',
                //     'Opera',
                //     'Navigator'
                // ]
                labels: doughnutLables
            },
            options: {
                animation: {
                    easing: 'easeOutBounce',
                },
                legend: {
                    display: true,
                    labels: {
                        boxWidth: 100,
                        usePointStyle: true
                    }
                }
            }
        })
    });

}(jQuery)