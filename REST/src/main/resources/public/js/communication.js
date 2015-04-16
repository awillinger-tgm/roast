$(function()
  {
    $('#tabs').tabs({
        beforeActivate: function(event, ui)
        {
            // clear out all input fields from their values
            $(ui.newPanel).find('input').each(function()
            {
                if(this.type == 'text')
                {
                    this.value = '';
                    
                    // and disable all input fields on the update tab (except ID)
                    if(ui.newPanel.selector == '#tabs-2' && this.id != 'existing_id')
                    {
                        $(this).prop('disabled', true);
                    }
                }
            });
            
            // same for the content area
            $(ui.newPanel).find('textarea').each(function()
            {
                if(ui.newPanel.selector == '#tabs-2') this.disabled = true;
                this.value = '';
            });
        }
    });
    
    $('#new_timestamp').datepicker({changeMonth: true, changeYear: true, dateFormat: "yy-mm-dd"});
    $('#existing_timestamp').datepicker({changeMonth: true, changeYear: true, dateFormat: "yy-mm-dd"});
    
    $('#new_send').click(function()
    {
        var title = $('#new_title');
        var content = $('#new_content');
        var timestamp = $('#new_timestamp');
        
        if(title.val() == '' || content.val() == '' || timestamp.val() == '')
        {
            alert('Please fill out all fields!');
            return;
        }
        
        var data = {
            title: title.val(),
            content: content.val(),
            timestamp: timestamp.val()
        };
        
        $.ajax({
            url: '/items',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify(data),
            type: 'POST',
        })
        .done(function(msg)
        {
            if(msg.success)
            {
                alert('Successfully added new item with ID '+msg.item.id);
            }
            else
            {
                alert('Failed to add new item\nError: '+msg.error);
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown)
        {
            alert('Failed to send data (general error)\nMessage: '+textStatus+' ('+errorThrown+')');
        });
    });
    
    $('#existing_send').click(function()
    {
        var id = $('#existing_id');
        
        var title = $('#existing_title');
        var content = $('#existing_content');
        var timestamp = $('#existing_timestamp');
        
        if(id.val() == '' || title.val() == '' || content.val() == '' || timestamp.val() == '')
        {
            alert('Please fill out all fields!');
            return;
        }
        
        var data = {
            title: title.val(),
            content: content.val(),
            timestamp: timestamp.val()
        };
        
        $.ajax({
            url: '/items?id='+id.val(),
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify(data),
            type: 'PUT',
        })
        .done(function(msg)
        {
            if(msg.success)
            {
                alert('Successfully updated item with ID '+msg.item.id);
            }
            else
            {
                alert('Failed to update item.\nError: '+msg.error);
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown)
        {
            alert('Failed to send data (general error)\nMessage: '+textStatus+' ('+errorThrown+')');
        });
    });
    
    $('#delete_send').click(function()
    {
        var id = $('#delete_id');

        if(id.val() == '')
        {
            alert('Please specfiy the ID of the item to be deleted');
            return;
        }
        
        $.ajax({
            url: '/items?id='+id.val(),
            type: 'DELETE',
        })
        .done(function(msg)
        {
            if(msg.success)
            {
                alert('Successfully deleted item with ID '+id.val());
                id.val('');
            }
            else
            {
                var error = msg.error;
                
                if(error.indexOf('Batch') > -1) error = 'The specified item does not exist';
                
                alert('Failed to delete item.\nError: '+error);
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown)
        {
            alert('Failed to send data (general error)\nMessage: '+textStatus+' ('+errorThrown+')');
        });
    });
    
    $('#existing_getItem').click(function()
    {
        var id = $('#existing_id');
        
        var title = $('#existing_title');
        var content = $('#existing_content');
        var timestamp = $('#existing_timestamp');
        var send = $('#existing_send');
        
        if(id.val() == '')
        {
            alert('Please enter the ID!');
            return;
        }
        
        $.ajax({
            url: '/items?id='+id.val(),
            type: 'GET',
        })
        .done(function(msg)
        {
            if(msg.success)
            {
                var date = new Date(msg.item.timestamp);
                
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                if(month < 10) month = '0'+month;
                var date = date.getDate();
                
                var dateValue =  year+'-'+month+'-'+date;
                
                title.val(msg.item.title);
                content.val(msg.item.content);
                timestamp.val(dateValue);
                
                title.prop('disabled', false);
                content.prop('disabled', false);
                timestamp.prop('disabled', false);
                send.prop('disabled', false);
            }
            else
            {
                alert('The specified item could not be found!');
                
                title.prop('disabled', true);
                content.prop('disabled', true);
                timestamp.prop('disabled', true);
                send.prop('disabled', true);
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown)
        {
            alert('Failed to request data (general error)\nMessage: '+textStatus+' ('+errorThrown+')');
        });
    });
  });