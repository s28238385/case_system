$(document).ready(function() {
    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }

    console.log(getCookie('userID'));

    var data_object = {
        'requester_id': getCookie('userID'),
        'applicant_id': 0,
        'case_id': 0,
    };

    // 將JSON格式轉換成字串
    var data_string = JSON.stringify(data_object);
    console.log(data_string);
    getCaseByID();

    function getCaseByID() {
        // 發出GET請求取得所有該會員的案件列表
        $.ajax({
            type: "GET",
            url: "api/case.do",
            crossDomain: true,
            data:data_string,
            cache: false,
            dataType: 'json',
            timeout: 5000,
            success: function (response) {
                if(response.status == 200){
                    updateTable(response.response.data);
                }
                console.log(response);
            },
            error: function () {
                alert("無法連線到伺服器！");
            }
        });
    }  

    // 更新案件列表表格
    function updateTable(data) {
        $("#table > tbody").empty();
        var table_html = '';
        $.each(data, function(index, value) {
            table_html +='<tr><td scope="row">' + value['case_id'] +'</td>';
            table_html +='<td>' + value['requester_id'] +'</td>';
            table_html +='<td>' + value['applicant_id'] +'</td>';
            table_html +='<td>' + value['applicated_time'] +'</td>';
            table_html +='<td>' + value['finished_time'] +'</td>';
            
            table_html +='<td>' + '<a id=edit href="case_edit.html?caseID=' + value['case_id'] + '">編輯</a>|';
            table_html +='<a id=delete href="javascript:deleteCase(' + value['case_id'] + ');">刪除</a></td>';

            table_html +='<td>' + '<a href="comment_for_requester.html?caseID=' + value['case_id'] + '">評價</a> ';
            table_html +='<td>' + '<a href="comment_to_requester.html?caseID=' + value['case_id'] + '">查看評價</a> ';
            table_html +='</td></tr>';
        })
        
        $("#table > tbody").append(table_html);
    }
});

function deleteCase(id) {
    var check = window.confirm("確認刪除案件？");
    if (check == true) {
        console.log("已確認刪除!");
        var request = {'case_id': id};
        var data_string = JSON.stringify(request);
        $.ajax({
            type: "DELETE",
            url: "api/progress.do",
            crossDomain: true,
            data: data_string,
            cache: false,
            dataType: 'json',
            timeout: 5000,
            success: function (response) {
                if(response.status == 200){
                    console.log(response);
                    window.location.reload();
                }
                console.log(response);
            },
            error: function () {
                alert("無法連線到伺服器！");
            }
        });
        $.ajax({
            type: "DELETE",
            url: "api/case.do",
            crossDomain: true,
            data: data_string,
            cache: false,
            dataType: 'json',
            timeout: 5000,
            success: function (response) {
                if(response.status == 200){
                    alert("成功刪除");
                    window.location.reload();
                    console.log(response);
                }
                console.log(response);
            },
            error: function () {
                alert("無法連線到伺服器！");
            }
        });
    }
    else {
        console.log("已取消!");
    }
}