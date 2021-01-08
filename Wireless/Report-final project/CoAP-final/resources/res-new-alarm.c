/*
 * Copyright (c) 2013, Institute for Pervasive Computing, ETH Zurich
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * This file is part of the Contiki operating system.
 */

/**
 * \file
 *      Example resource
 * \author
 *      Matthias Kovatsch <kovatsch@inf.ethz.ch>
 */

#include <stdlib.h>
#include <string.h>
#include "rest-engine.h"

#include "extern_var.h"

static void res_get_handler(void *request, void *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);

/*
 * A handler function named [resource name]_handler must be implemented for each RESOURCE.
 * A buffer for the response payload is provided through the buffer pointer. Simple resources can ignore
 * preferred_size and offset, but must respect the REST_MAX_CHUNK_SIZE limit for the buffer.
 * If a smaller block size is requested for CoAP, the REST framework automatically splits the data.
 */
RESOURCE(res_new_alarm,
         "title=\"ALARM\"",
         res_get_handler,
         NULL,
         NULL,
         NULL);

static void
res_get_handler(void *request, void *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
  printf("Light is received by alarm : %d\n",light_info);
  printf("Temperature is received by alarm : %d\n",temper_info);
  printf("Accleration is received by alarm : %d, %d, %d\n",acc_x, acc_y, acc_z);
  const char *len = NULL;
  int alarm_info = 0;

  if(acc_x_old ==0){
	  acc_x_old = acc_x;
  }
  printf("Last accleration in x dimension is received by alarm : %d\n",acc_x_old);

  if(light_info ==1){
	  printf("###########       ###########\n");
	  printf("###Light###------>###ALARM###\n");
	  printf("###########       ###########\n");
	  alarm_info = 1;
  }
  if(temper_info >30 || (temper_info < 0 && temper_info != -1000)){
  	  printf("#################       ###########\n");
	  printf("###Temperature###------>###ALARM###\n");
	  printf("#################       ###########\n");
	  alarm_info = 1;
  }

  int dif = acc_x - acc_x_old;

  if(acc_x > 90 || acc_x < -90 || dif >8 || dif < -8){
  	  printf("##################       ###########\n");
	  printf("###Acceleration###------>###ALARM###\n");
	  printf("##################       ###########\n");
	  alarm_info = 1;
  }
  
  if(alarm_info == 0){
	  REST.set_header_content_type(response, REST.type.TEXT_PLAIN); /* text/plain is the default, hence this option could be omitted. */
          snprintf((char*)buffer, REST_MAX_CHUNK_SIZE,"The ALARM is OFF \n");
  }else{
          REST.set_header_content_type(response, REST.type.TEXT_PLAIN); /* text/plain is the default, hence this option could be omitted. */
          snprintf((char*)buffer, REST_MAX_CHUNK_SIZE,"The ALARM is ON !!!\n");
  
  }

//  int random = 0;
//  random = rand() % 5;
//  if(random>0)
//	  random = 0;
//  else
//	  random = 1;

  REST.set_response_payload(response, (int*)buffer, strlen((char*)buffer));
}
