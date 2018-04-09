
        var updatingId='';
        var fkcliente='';
        var fkpratica='';

        $(document).ready(function(){
          homeTabClick();
          getMioNome();
          userOrAdmin();
          updateScadenze();
        });


        function aphostrofeReplace(str){
            //return str.replace(/'/g, '\'');
            return str.replace(/'/g, "&rsquo;")
        }

        function userOrAdmin(){
            $.ajax({
            url: '/user/or/admin',
            type: "GET",
            success: function (data, status, xhr) {
                console.log(data);
                if(data == 'ROLE_USER'){
                    $(".only_admin").remove();
                }
            },
            error: function(result) {
                alert("Errore: " + result.responseJSON.error);
                console.log(result);
            }
           });
        }

        $("#btnSubmit").click(function (event) {
             event.preventDefault();
             $("#file_upload_step").val( $("#stepPraticaDocumentoSelect").val() );
             fire_ajax_submit();
        });

        function fire_ajax_submit() {
            $("#result_caricamento").text("");
            $("#percorsofile_documento").text("");
            $("#download_documento").prop('href', '#');
            var form = $('#fileUploadForm')[0];
            var data = new FormData(form);
            $("#btnSubmit").prop("disabled", true);
            $.ajax({
                type: "POST",
                enctype: 'multipart/form-data',
                url: "/api/upload",
                data: data,
                processData: false, //prevent jQuery from automatically transforming the data into a query string
                contentType: false,
                cache: false,
                timeout: 600000,
                success: function (data) {
                    $("#result_caricamento").text("Documento caricato: ");
                    $("#percorsofile_documento").text(data);
                    $("#download_documento").prop('href', '/getfile?filename=' + data);
                    console.log("SUCCESS : ", data);
                    $("#btnSubmit").prop("disabled", false);
                },
                error: function (e) {
                    alert(e.responseText);
                    console.log("ERROR : ", e);
                    $("#btnSubmit").prop("disabled", false);
                }
            });
        }

        function downloadClick(){
            var file = $("#percorsofile_documento").text();
            if( file == '' || file == null){
                alert("Nessun percorso file selezionato!");
            }else{
                $.ajax({
                   url: '/getfile',
                   type: "GET",
                   data: {
                     filename:  file
                   },
                   success: function (data, status, xhr) {
                      console.log(data);
                   },
                   error: function(result) {
                      alert("Errore: " + result.responseJSON.message);
                      console.log(result);
                   }
               });
            }
        }

        function showUtenti(){
           $.ajax({
            url: '/get/utente',
            type: "GET",
            success: function (data, status, xhr) {
                console.log(data);
                $("#body_tabella_utenti").empty();
                jQuery.each(data, function(i, val) {
                    $("#body_tabella_utenti").append("<tr><th class='finger' onclick='seeUtente(\"" + val.codiceFiscale + "\", \"" + aphostrofeReplace(val.nominativo) + "\", \"" + aphostrofeReplace(val.indirizzo) + "\", \"" + val.telefono + "\", \"" + val.email + "\", \"" + val.natoIl + "\", \"" + val.ruolo + "\")'><a href='#form_utente'>" + val.codiceFiscale + "</a></th><td>" + val.nominativo + "</td><td>" + val.email + "</td><td>" + val.ruolo + "</td></tr>");
                });
            },
            error: function(result) {
                alert("Errore: " + result.responseJSON.error);
                console.log(result);
            }
           });
        }

        function getMioNome(){
           $.ajax({
                url: '/get/mio/nome',
                type: "GET",
                success: function (data, status, xhr) {
                    console.log(data);
                    $("#mionome").text(data);
                },
                error: function(result) {
                    console.log(result);
                }
           });
        }

        function showScadenze(){
           $.ajax({
            url: '/get/scadenza',
            type: "GET",
            success: function (data, status, xhr) {
                console.log(data);
                $("#body_tabella_scadenze").empty();
                jQuery.each(data, function(i, val) {
                    $("#body_tabella_scadenze").append("<tr class='" + val.avvisoDa + "'><th>" + val.descrizione + "</th><td>" + convertDate(val.dataScadenza) + "</td><td class='finger' onclick='goToCliente(\"" + val.fkCliente + "\")'>" + val.fkCliente + "</td><td><p class='delete' onclick='deleteScadenza(" + val.idScadenza + ")'></p></td></tr>");
                });
            },
            error: function(result) {
                alert("Errore: " + result.responseJSON.error);
                console.log(result);
            }
           });
        }

        function visualizzaTutteScadenze(){
            $.ajax({
                url: '/get/all/scadenza',
                type: "GET",
                success: function (data, status, xhr) {
                    console.log(data);
                    $("#body_tabella_scadenze").empty();
                    jQuery.each(data, function(i, val) {
                        $("#body_tabella_scadenze").append("<tr class='" + val.avvisoDa + "'><th>" + val.descrizione + "</th><td>" + convertDate(val.dataScadenza) + "</td><td class='finger' onclick='goToCliente(\"" + val.fkCliente + "\")'>" + val.fkCliente + "</td><td><p class='delete' onclick='deleteScadenza(" + val.idScadenza + ")'></p></td></tr>");
                    });
                },
                error: function(result) {
                    alert("Errore: " + result.responseJSON.error);
                    console.log(result);
                }
           });
        }

        function deleteScadenza(idScadenza){
            $.ajax({
            url: '/delete/scadenza/' + idScadenza,
            type: "GET",
            success: function (data, status, xhr) {
                console.log(data);
                showScadenze();
            },
            error: function(result) {
                alert("Errore: " + result.responseJSON.error);
                console.log(result);
            }
           });
        }

        function updateScadenze(){
            $.ajax({
                url: '/delete/old/scadenza',
                type: "GET",
                success: function (data, status, xhr) {
                    console.log(data);
                },
                error: function(result) {
                    console.log(result);
                }
           });
        }

        function goToCliente(fkCliente){
           clientiTabClick();
           showClienti('/get/cliente/id/' + fkCliente);
        }

        function cercaClienti(){
            var parametroRicerca = $("#cercaClientiInput").val();
            var tipoRicerca = $("#cercaClientiSelect").val();
            var urlComposto = '/get/cliente';
            emptyForms();
            $("#tabella_pratiche").hide();
            $("#tabella_documenti").hide();
            if(tipoRicerca == 'nominativo'){
                urlComposto = '/get/cliente/nominativo/' + parametroRicerca;
            }else if(tipoRicerca == 'ragionesociale'){
                urlComposto = '/get/cliente/ragionesociale/' + parametroRicerca;
            }else if(tipoRicerca == 'attivita'){
                urlComposto = '/get/cliente/attivita/' + parametroRicerca;
            }else if(tipoRicerca == 'segnalatore'){
                urlComposto = '/get/cliente/segnalatore/' + parametroRicerca;
            }else if(tipoRicerca == 'interessatoa'){
                urlComposto = '/get/cliente/interessatoa/' + parametroRicerca;
            }else if(tipoRicerca == 'id'){
                urlComposto = '/get/cliente/id/' + parametroRicerca;
            }
            showClienti(urlComposto);
        }

        function showClienti(urlComposto){
           $.ajax({
            url: urlComposto,
            type: "GET",
            success: function (data, status, xhr) {
                console.log(data);
                $("#body_tabella_clienti").empty();
                jQuery.each(data, function(i, val) {
                    console.log("NATURAL: " + val.ragioneSociale);
                    console.log("TREATED: " + aphostrofeReplace(val.ragioneSociale));
                    $("#body_tabella_clienti").append("<tr id='istanza_cliente_" + val.idCliente + "' class='generic_cliente'><th  class='finger' onclick='seeCliente(\"" + val.idCliente + "\", \"" + aphostrofeReplace(val.ragioneSociale) + "\", \"" + aphostrofeReplace(val.nominativo) + "\", \"" + aphostrofeReplace(val.indirizzo) + "\", \"" + val.telefono + "\", \"" + val.email + "\", \"" + val.sito + "\", \"" + aphostrofeReplace(val.segnalatore) + "\", \"" + aphostrofeReplace(val.attivita) + "\", \"" + aphostrofeReplace(val.interessatoA) + "\", \"" + val.registratoDa + "\", \"" + val.data + "\", \"" + aphostrofeReplace(val.note) + "\")'><a href='#form_cliente'>" + val.idCliente + "</a></th><td>" + val.ragioneSociale + "</td><td>" + val.nominativo + "</td><td>" + val.attivita + "</td><td class='finger' style='text-align: center;'><i class='fa fa-folder fa-2x has-text-info' onclick='showPratiche(\"" + val.idCliente + "\")'></i></td><td class='finger' style='text-align: center;'><i class='far fa-calendar-alt fa-2x has-text-warning' onclick='nuovaScadenza(\"" + val.idCliente + "\")'></i></td></tr>");
                });
            },
            error: function(result) {
                alert("Errore: " + result.responseJSON.error);
                console.log(result);
            }
           });
        }

        function showPratiche(idCliente){
           emptyForms();
           $("#istanza_cliente_" + idCliente).removeClass("generic_cliente");
           $(".generic_cliente").empty();
           $("#tabella_pratiche").show();
           fkcliente=idCliente;
           $("#file_upload_cliente").val(fkcliente);
           $.ajax({
            url: '/get/pratica/cliente/' + idCliente,
            type: "GET",
            success: function (data, status, xhr) {
                console.log(data);
                $("#body_tabella_pratiche").empty();
                jQuery.each(data, function(i, val) {
                    $("#body_tabella_pratiche").append("<tr id='istanza_pratica_" + i + "' class='generic_pratica'><th  class='finger' onclick='seePratica(\"" + val.idPratica + "\", \"" + aphostrofeReplace(val.descrizione) + "\", \"" + aphostrofeReplace(val.note) + "\", \"" + val.data +"\", \"" + val.fkCliente + "\", \"" + val.registratoDa + "\")'><a href='#form_pratica'>" + val.idPratica + "</a></th><td>" + val.descrizione + "</td><td>" + convertDate(val.data) + "</td><td  class='finger' style='text-align: center;'><i class='fa fa-folder fa-2x has-text-success' onclick='showDocumenti(\"" + val.idPratica + "\", \"" + i + "\")'> </i></td></tr>");
                });
            },
            error: function(result) {
                alert("Errore: " + result.responseJSON.error);
                console.log(result);
            }
           });
        }

        function showDocumenti(idPratica, i){
           emptyForms();
           $("#istanza_pratica_" + i).removeClass("generic_pratica");
           $(".generic_pratica").empty();
           $("#tabella_documenti").show();
           fkpratica=idPratica;
           $("#file_upload_pratica").val(fkpratica);
           $.ajax({
            url: '/get/documento/pratica/' + idPratica,
            type: "GET",
            success: function (data, status, xhr) {
                console.log(data);
                $("#body_tabella_documenti").empty();
                jQuery.each(data, function(i, val) {
                    $("#body_tabella_documenti").append("<tr><th  class='finger' onclick='seeDocumento(\"" + val.idDocumento + "\", \"" + aphostrofeReplace(val.descrizione) + "\", \"" + val.percorsoFile + "\", \"" + aphostrofeReplace(val.note) + "\", \"" + val.stepPratica + "\", \"" + val.fkPratica + "\", \"" + val.registratoDa + "\", \"" + val.data + "\")'><a href='#form_documento'>" + val.descrizione + "</a></th><td>" + val.stepPratica + "</td><td>" + convertDate(val.data) + "</td></tr>");
                });
            },
            error: function(result) {
                alert("Errore: " + result.responseJSON.error);
                console.log(result);
            }
           });
        }

        function cambioPassword(){
            emptyForms();
            $("#form_cambia_password_utente").show();
        }

        function seeUtente(codiceFiscale, nominativo, indirizzo, telefono, email, natoIl, ruolo){
            emptyForms();
            $(".hidden_form").show();
            $(".submit").hide();   //hide submit and show with modificaclick() con cui si riabilitano tutti gli input text
            $('.container input[type="text"]').prop('disabled', true);
            $("#form_utente").show();
            $("#codicefiscale_utente").val(codiceFiscale);
            $("#nominativo_utente").val(nominativo);
            $("#indirizzo_utente").val(indirizzo);
            $("#telefono_utente").val(telefono);
            $("#email_utente").val(email);
            $("#natoil_utente").val(convertDate(natoIl));
        }

        function seeCliente(idCliente, ragioneSociale, nominativo, indirizzo, telefono, email, sito, segnalatore, attivita, interessatoA, registratoDa, data, note){
            emptyForms();
            $(".hidden_form").show();
            $(".submit").hide();   //hide submit and show with modificaclick() con cui si riabilitano tutti gli input text
            $('.container input[type="text"]').prop('disabled', true);
            $("#form_cliente").show();
            $("#idcliente_cliente").val(idCliente);
            $("#ragionesociale_cliente").val(ragioneSociale);
            $("#nominativo_cliente").val(nominativo);
            $("#indirizzo_cliente").val(indirizzo);
            $("#telefono_cliente").val(telefono);
            $("#email_cliente").val(email);
            $("#sito_cliente").val(sito);
            $("#segnalatore_cliente").val(segnalatore);
            $("#attivita_cliente").val(attivita);
            $("#interessatoa_cliente").val(interessatoA);
            $("#registratoda_cliente").val(registratoDa);
            $("#data_cliente").val(convertDate(data));
            $("#note_cliente").val(note);
        }


        function seePratica(idPratica, descrizione, note, data, fkCliente, registratoDa){
            emptyForms();
            $(".hidden_form").show();
            $(".submit").hide();   //hide submit and show with modificaclick() con cui si riabilitano tutti gli input text
            $('.container input[type="text"]').prop('disabled', true);
            $("#form_pratica").show();
            $("#idpratica_pratica").val(idPratica);
            $("#descrizione_pratica").val(descrizione);
            $("#note_pratica").val(note);
            $("#data_pratica").val(convertDate(data));
            $("#fkcliente_pratica").text(fkCliente);
            $("#registratoda_pratica").val(registratoDa);
        }

        function seeDocumento(idDocumento, descrizione, percorsoFile, note, stepPratica, fkPratica, registratoDa, data){
            console.log("IDDOCUMENTO: " + idDocumento);
            emptyForms();
            $(".hidden_form").show();
            $(".submit").hide();   //hide submit and show with modificaclick() con cui si riabilitano tutti gli input text
            $('.container input[type="text"]').prop('disabled', true);
            $("#form_documento").show();
            $("#iddocumento_documento").text(idDocumento);
            $("#descrizione_documento").val(descrizione);
            $("#note_documento").val(note);
            $("#percorsofile_documento").text(percorsoFile);
            $("#download_documento").prop('href', '/getfile?filename=' + percorsoFile);
            $("#stepPraticaDocumentoSelect").val(stepPratica);
            $("#file_upload_step").val(stepPratica);
            $("#fkpratica_documento").text(fkPratica);
            $("#registratoda_documento").val(registratoDa);
            $("#data_documento").val(convertDate(data));
        }

        function modificaClick(caller){
            $(".submit").show();
            $(".modifica").hide();
            $('.container input[type="text"]').prop('disabled', false);
            if(caller == 'utente'){
                updatingId = $("#codicefiscale_utente").val();
            }else if(caller == 'cliente'){
                updatingId = $("#idcliente_cliente").val();
            }else if(caller == 'pratica'){
                updatingId = $("#idpratica_pratica").val();
            }
        }


        function submitUtente(){
            //raccolta dati e chiamata AJAX
            $.ajax({
               url: '/update/utente',
               type: "POST",
               data: {
                 oldId:         updatingId,
                 codiceFiscale: $("#codicefiscale_utente").val(),
                 nominativo:    $("#nominativo_utente").val(),
                 indirizzo:     $("#indirizzo_utente").val(),
                 telefono:      $("#telefono_utente").val(),
                 email:         $("#email_utente").val(),
                 natoIl:        $("#natoil_utente").val(),
                 password:      '1234'
               },
               success: function (data, status, xhr) {
                  console.log(data);
                  emptyForms();
                  $("#form_utente").hide();
                  showUtenti();
               },
               error: function(result) {
                  alert("Errore: " + result.responseJSON.message);
                  console.log(result);
               }
           });
        }

        function submitPasswordUtente(){
            //raccolta dati e chiamata AJAX
            var vecchiaPassword = $("#vecchia_password_utente").val();
            var nuovaPassword = $("#cambiapwd_password_utente").val();
            var confermaPassword = $("#cambiapwd_confermapassword_utente").val();
            if(nuovaPassword == confermaPassword){
                $.ajax({
               url: '/password',
               type: "POST",
               data: {
                 vecchiapassword: $("#vecchia_password_utente").val(),
                 nuovapassword:   $("#cambiapwd_password_utente").val()
               },
               success: function (data, status, xhr) {
                  console.log(data);
                  showUtenti();
                  alert("La tua password e' stata modificata con successo.");
                  $("#vecchia_password_utente").val('');
                  $("#cambiapwd_password_utente").val('');
                  $("#cambiapwd_confermapassword_utente").val('');
                  $("#form_cambia_password_utente").hide();
               },
               error: function(result) {
                  alert("Errore: " + result.responseJSON.message);
                  console.log(result);
               }
           });
            } else {
                alert("Le due password non coincidono!");
            }

        }

        function submitCliente(){
            //raccolta dati e chiamata AJAX
            $.ajax({
               url: '/update/cliente',
               type: "POST",
               data: {
                 oldId:             updatingId,
                 idCliente:         $("#idcliente_cliente").val(),
                 ragioneSociale:    $("#ragionesociale_cliente").val(),
                 nominativo:        $("#nominativo_cliente").val(),
                 indirizzo:         $("#indirizzo_cliente").val(),
                 telefono:          $("#telefono_cliente").val(),
                 email:             $("#email_cliente").val(),
                 sito:              $("#sito_cliente").val(),
                 segnalatore:       $("#segnalatore_cliente").val(),
                 attivita:          $("#attivita_cliente").val(),
                 interessatoA:      $("#interessatoa_cliente").val(),
                 note:              $("#note_cliente").val()
               },
               success: function (data, status, xhr) {
                  console.log(data);
                  alert("Aggiornamento contatti avvenuto con successo!");
                  resetFormsAndTables();
                  clientiTabClick();
               },
               error: function(result) {
                  alert("Errore: " + result.responseJSON.message);
                  console.log(result);
               }
           });

        }

        function submitPratica(){
            //raccolta dati e chiamata AJAX
            $.ajax({
               url: '/update/pratica',
               type: "POST",
               data: {
                 oldId:             updatingId,
                 idPratica:         $("#idpratica_pratica").val(),
                 note:              $("#note_pratica").val(),
                 descrizione:       $("#descrizione_pratica").val(),
                 fkCliente:         $("#fkcliente_pratica").text()
               },
               success: function (data, status, xhr) {
                  console.log(data);
                  alert("Aggiornamento pratiche avvenuto con successo!");
                  resetFormsAndTables();
                  clientiTabClick();
               },
               error: function(result) {
                  alert("Errore: " + result.responseJSON.message);
                  console.log(result);
               }
            });
        }

        function submitDocumento(){
            //raccolta dati e chiamata AJAX
            $.ajax({
               url: '/update/documento',
               type: "POST",
               data: {
                 idDocumento:       $("#iddocumento_documento").text(),
                 descrizione:       $("#descrizione_documento").val(),
                 percorsoFile:      $("#percorsofile_documento").text(),
                 note:              $("#note_documento").val(),
                 stepPratica:       $("#stepPraticaDocumentoSelect").val(),
                 fkPratica:         $("#fkpratica_documento").text(),
               },
               success: function (data, status, xhr) {
                  console.log(data);
                  alert("Aggiornamento pratiche avvenuto con successo!");
                  resetFormsAndTables();
                  clientiTabClick();
               },
               error: function(result) {
                  alert("Errore: " + result.responseJSON.message);
                  console.log(result);
               }
            });
        }

        function submitScadenza(){
            //raccolta dati e chiamata AJAX
            $.ajax({
               url: '/save/scadenza',
               type: "POST",
               data: {
                 descrizione:       $("#descrizione_scadenza").val(),
                 dataScadenza:      $("#datascadenza_scadenza").val(),
                 fkCliente:         $("#fkcliente_scadenza").text(),
                 avvisoDa:          $("#avvisoDaSelect").val()
               },
               success: function (data, status, xhr) {
                  console.log(data);
                  alert("Scadenza inserita con successo!");
                  chiudiModalNuovaScadenza();
                  resetFormsAndTables();
                  clientiTabClick();
               },
               error: function(result) {
                  alert("Errore: " + result.responseJSON.message);
                  console.log(result);
               }
            });
        }

        function nuovoUtente(){
            emptyForms();
            $("#form_utente").show();
            alert("ATTENZIONE: quando un nuovo utente del sistema e' generato, viene impostata la password di base 1234");
        }
        function nuovoCliente(){
            emptyForms();
            $("#form_cliente").show();
        }
        function nuovaPratica(){
            emptyForms();
            $("#form_pratica").show();
            $("#fkcliente_pratica").text(fkcliente);
            console.log("FKCLIENTE: " + fkcliente);
        }
        function nuovoDocumento(){
            emptyForms();
            $("#form_documento").show();
            $("#fkpratica_documento").text(fkpratica);
            console.log("FKPRATICA: " + fkpratica);
        }

        function nuovaScadenza(fkCliente){      //apre il modal per inserire la nuova scadenza
            $(".always_enabled").prop("disabled", false);
            $("#fkcliente_scadenza").text(fkCliente);
            $("#modal_nuova_scadenza").addClass("is-active");
        }

        function chiudiModalNuovaScadenza(){    //chiude il modal per inserire la nuova scadenza
            $("#fkcliente_scadenza").text('');
            $("#modal_nuova_scadenza").removeClass("is-active");
        }

        function resetFormsAndTables(){
            $("#home_div").hide();
            $("#contatti_div").hide();
            $("#tabella_utenti").hide();
            $("#tabella_scadenze").hide();
            $("#tabella_clienti").hide();
            $("#tabella_pratiche").hide();
            $("#tabella_documenti").hide();
            $("#form_utente").hide();
            $("#form_cliente").hide();
            $("#form_cambia_password_utente").hide();
            $("#form_pratica").hide();
            $("#form_documento").hide();
            $(".tab").removeClass("is-active");
            $("#body_tabella_utenti").empty();
            $("#body_tabella_scadenze").empty();
            $("#body_tabella_clienti").empty();
            $("#body_tabella_pratiche").empty();
            $("#body_tabella_documenti").empty();
            $("#cercaClientiInput").val('');
            $('.container input[type="text"]').val('');
            $('.container input[type="password"]').val('');
            $('.container label').text('');
            $(".hidden_form").hide();
            $('.container input[type="text"]').prop('disabled', false);
            $(".submit").show();
            updatingId='';
            fkcliente='';
            fkpratica='';
            $("#file_upload_cliente").val('');
            $("#file_upload_pratica").val('');
            $("#file_upload_step").val('');
            $("#file_upload_documento").val('');
            $('.container input[type="file"]').val('');
            //$(".navbar-menu").removeClass("is-active");
            //$(".navbar-burger").removeClass("is-active")
        }

        function emptyForms(){
            $("#form_utente").hide();
            $("#form_cliente").hide();
            $("#form_cambia_password_utente").hide();
            $("#form_pratica").hide();
            $("#form_documento").hide();
            $('.container input[type="text"]').val('');
            $('.container input[type="password"]').val('');
            $('.container label').text('');
            $('.container input[type="text"]').prop('disabled', false);
            $(".hidden_form").hide();
            $(".submit").show();
            updatingId='';
            $('.container input[type="file"]').val('');
            //$(".navbar-menu").removeClass("is-active");
            //$(".navbar-burger").removeClass("is-active")
        }


        function homeTabClick(){
            resetFormsAndTables();
            $("#home_tab").addClass("is-active");
            $("#home_div").show();
        }
        function utentiTabClick(){
            resetFormsAndTables();
            $("#utenti_tab").addClass("is-active");
            showUtenti();
            $("#tabella_utenti").show();
        }
        function scadenzeTabClick(){
            resetFormsAndTables();
            $("#scadenze_tab").addClass("is-active");
            showScadenze();
            $("#tabella_scadenze").show();
        }
        function clientiTabClick(){
            resetFormsAndTables();
            $("#clienti_tab").addClass("is-active");
            $("#tabella_clienti").show();
        }
        function contattiTabClick(){
            resetFormsAndTables();
            $("#contatti_tab").addClass("is-active");
            $("#contatti_div").show();
        }
        function convertDate(inputFormat) {
            function pad(s) { return (s < 10) ? '0' + s : s; }
            var d = new Date(inputFormat);
            return [pad(d.getDate()), pad(d.getMonth()+1), d.getFullYear()].join('/');
        }
        function logoutClick(){
            resetFormsAndTables();
            $.ajax({
                url: '/logout',
                type: "GET",
                success: function (data, status, xhr) {
                  console.log(data);
                  location.reload();
                },
                error: function(result) {
                  alert("Errore: " + result.responseJSON.error);
                  console.log(result);
                }
           });
        }

        //per il "burger menu' visibile nei dispositivi mobili"
        /*document.addEventListener('DOMContentLoaded', function () {
            var $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);
            if ($navbarBurgers.length > 0) {
                $navbarBurgers.forEach(function ($el) {
                    $el.addEventListener('click', function () {
                        var target = $el.dataset.target;
                        var $target = document.getElementById(target);
                        $el.classList.toggle('is-active');
                        $target.classList.toggle('is-active');
                    });
                });
            }
        });*/

        // inserire un piccolo sistema di log
        // inserire le dipendenze per mysql
        // vedere se è possibile utilizzare un form di login custom, anche a costo di usare un altro controller
        // dopo un inserimento corretto provare a non chiudere tutti i tab
        // spostare il javascript in un file dedicato


